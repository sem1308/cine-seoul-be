
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
import uos.cineseoul.repository.PaymentMethodRepository;
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
    private final PaymentMethodRepository paymentMethodRepo;
    private final TicketRepository ticketRepo;
    private final UserRepository userRepo;
    private final AccountService accountService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepo, TicketRepository ticketRepo,UserRepository userRepo, PaymentMethodRepository paymentMethodRepo, AccountService accountService) {
        this.paymentRepo = paymentRepo;
        this.ticketRepo = ticketRepo;
        this.userRepo = userRepo;
        this.paymentMethodRepo = paymentMethodRepo;
        this.accountService = accountService;
    }

    public List<PrintPaymentDTO> findAll() {
        List<Payment> paymentList = paymentRepo.findAll();
        if (paymentList.isEmpty()) {
            throw new ResourceNotFoundException("결제 내역이 없습니다.");
        }
        return getPrintDTOList(paymentList);
    }

    public PrintPaymentDTO findOneByNum(Long num) {
        Payment payment = paymentRepo.findById(num).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ num +"인 결제 내역이 없습니다.");
        });
        return getPrintDTO(payment);
    }
    public List<PrintPaymentDTO> findByUserNum(Long userNum) {
        List<Payment> paymentList = paymentRepo.findByUserNum(userNum);
        if (paymentList.isEmpty()) {
            throw new ResourceNotFoundException(userNum+"번 유저에 대한 결제 내역이 없습니다.");
        }
        return getPrintDTOList(paymentList);
    }
    public List<PrintPaymentDTO> findByUserId(String userId) {
        List<Payment> paymentList = paymentRepo.findByUserId(userId);
        if (paymentList.isEmpty()) {
            throw new ResourceNotFoundException("유저 "+userId+"에 대한 결제 내역이 없습니다.");
        }
        return getPrintDTOList(paymentList);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public PrintPaymentDTO insert(InsertPaymentDTO paymentDTO) {
        Payment payment = PaymentMapper.INSTANCE.toEntity(paymentDTO);

        User user = userRepo.findById(paymentDTO.getUserNum()).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ paymentDTO.getUserNum() +"인 사용자가 없습니다.");
        });

        Ticket ticket = ticketRepo.findById(paymentDTO.getTicketNum()).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ paymentDTO.getTicketNum() +"인 티켓이 없습니다.");
        });
        
        // 결제 가격과 티켓 판매 가격 일치여부 확인
        if(!ticket.getSalePrice().equals(payment.getPrice())){
            throw new DataInconsistencyException("결제 가격과 티켓 판매 가격이 일치하지 않습니다.");
        }

        // 이미 발행된 티켓인지 확인
        if(ticket.getIssued().equals(TicketState.Y)){
            throw new ResourceNotFoundException("이미 발행된 티켓입니다.");
        }else{
            ticket.setIssued(TicketState.Y);
            ticketRepo.save(ticket);
        }

        PaymentMethod pm = paymentMethodRepo.findById(paymentDTO.getPaymentMethodCode()).orElseThrow(()->{
            throw new ResourceNotFoundException(paymentDTO.getPaymentMethodCode() +"인 결제 방법이 없습니다.");
        });

        switch (pm.getPaymentMethodCode().toString().indexOf(0)){
            case 'A':
                accountService.payByAccountNum(paymentDTO.getPrice(),paymentDTO.getAccountNum());
                break;
            case 'C':
                accountService.payByCardNum(paymentDTO.getPrice(),user.getName(),paymentDTO.getCardNum());
                break;
        }
        if(pm.getName().equals("card")){
            accountService.checkVaildity(paymentDTO.getCardNum(),user.getName());
            payment.setApprovalNum(accountService.getApprovalNum(paymentDTO.getCardNum()));
        }

        payment.setUser(user);
        payment.setTicket(ticket);
        payment.setPaymentMethod(pm);
        payment.setState(PayState.Y);

        Payment savedPayment = paymentRepo.save(payment);

        return getPrintDTO(savedPayment);
    }

    private PrintPaymentDTO getPrintDTO(Payment payment){
        PrintPaymentDTO paymentDTO = PaymentMapper.INSTANCE.toDTO(payment);
        paymentDTO.setTicketNum(payment.getTicket().getTicketNum());
        return paymentDTO;
    }

    private List<PrintPaymentDTO> getPrintDTOList(List<Payment> paymentList){
        List<PrintPaymentDTO> pPaymentList = new ArrayList<>();
        paymentList.forEach(payment -> {
            pPaymentList.add(getPrintDTO(payment));
        });
        return pPaymentList;
    }
}
