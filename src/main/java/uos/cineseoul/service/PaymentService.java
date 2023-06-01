
package uos.cineseoul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertPaymentDTO;
import uos.cineseoul.dto.response.PrintPaymentDTO;
import uos.cineseoul.entity.*;
import uos.cineseoul.exception.DataInconsistencyException;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.PaymentMapper;
import uos.cineseoul.repository.PaymentRepository;
import uos.cineseoul.repository.TicketRepository;
import uos.cineseoul.repository.UserRepository;
import uos.cineseoul.utils.enums.PayState;
import uos.cineseoul.utils.enums.TicketState;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepo;
    private final TicketRepository ticketRepo;
    private final AccountService accountService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepo, TicketRepository ticketRepo,AccountService accountService) {
        this.paymentRepo = paymentRepo;
        this.ticketRepo = ticketRepo;
        this.accountService = accountService;
    }

    public List<Payment> findAll() {
        List<Payment> paymentList = paymentRepo.findAll();
        if (paymentList.isEmpty()) {
            throw new ResourceNotFoundException("결제 내역이 없습니다.");
        }
        return paymentList;
    }

    public Payment findOneByNum(Long num) {
        Payment payment = paymentRepo.findById(num).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ num +"인 결제 내역이 없습니다.");
        });
        return payment;
    }
    public List<Payment> findByUserNum(Long userNum) {
        List<Payment> paymentList = paymentRepo.findByUserNum(userNum);
        if (paymentList.isEmpty()) {
            throw new ResourceNotFoundException(userNum+"번 유저에 대한 결제 내역이 없습니다.");
        }
        return paymentList;
    }
    public List<Payment> findByUserId(String userId) {
        List<Payment> paymentList = paymentRepo.findByUserId(userId);
        if (paymentList.isEmpty()) {
            throw new ResourceNotFoundException("유저 "+userId+"에 대한 결제 내역이 없습니다.");
        }
        return paymentList;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Payment insert(InsertPaymentDTO paymentDTO) {
        Payment payment = PaymentMapper.INSTANCE.toEntity(paymentDTO);
        Ticket ticket = payment.getTicket();
        User user = payment.getUser();

        // 결제 가격과 티켓 판매 가격 일치여부 확인
        if(!ticket.getSalePrice().equals(payment.getPrice())){
            throw new DataInconsistencyException("결제 가격과 티켓 판매 가격이 일치하지 않습니다.");
        }

        // 이미 발행된 티켓인지 확인
        if(ticket.getTicketState().equals(TicketState.Y)){
            throw new ResourceNotFoundException("이미 발행된 티켓입니다.");
        }else{
            ticket.setTicketState(TicketState.Y);
            ticketRepo.save(ticket);
        }

        switch (payment.getPaymentMethod().toString().indexOf(0)){
            case 'A':
                accountService.payByAccountNum(paymentDTO.getPrice(),paymentDTO.getAccountNum());
                break;
            case 'C':
                accountService.payByCardNum(paymentDTO.getPrice(),user.getName(),paymentDTO.getCardNum());
                accountService.checkVaildity(paymentDTO.getCardNum(),user.getName());
                payment.setApprovalNum(accountService.getApprovalNum(paymentDTO.getCardNum()));
                break;
        }

        Payment savedPayment = paymentRepo.save(payment);

        return savedPayment;
    }

    public PrintPaymentDTO getPrintDTO(Payment payment){
        PrintPaymentDTO paymentDTO = PaymentMapper.INSTANCE.toDTO(payment);
        paymentDTO.setTicketNum(payment.getTicket().getTicketNum());
        return paymentDTO;
    }

    public List<PrintPaymentDTO> getPrintDTOList(List<Payment> paymentList){
        List<PrintPaymentDTO> pPaymentList = new ArrayList<>();
        paymentList.forEach(payment -> {
            pPaymentList.add(getPrintDTO(payment));
        });
        return pPaymentList;
    }
}
