
package uos.cineseoul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.InsertPaymentDTO;
import uos.cineseoul.entity.*;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.PaymentMapper;
import uos.cineseoul.repository.PaymentMethodRepository;
import uos.cineseoul.repository.PaymentRepository;
import uos.cineseoul.repository.TicketRepository;
import uos.cineseoul.repository.UserRepository;

import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepo;
    private final PaymentMethodRepository paymentMethodRepo;
    private final TicketRepository ticketRepo;
    private final UserRepository userRepo;

    @Autowired
    public PaymentService(PaymentRepository paymentRepo, TicketRepository ticketRepo,UserRepository userRepo, PaymentMethodRepository paymentMethodRepo) {
        this.paymentRepo = paymentRepo;
        this.ticketRepo = ticketRepo;
        this.userRepo = userRepo;
        this.paymentMethodRepo = paymentMethodRepo;
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

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Payment insert(InsertPaymentDTO paymentDTO) {
        Payment payment = PaymentMapper.INSTANCE.toEntity(paymentDTO);

        // TODO : 결제방법이 카드일 때 승인번호 받아오기
        if(paymentDTO.getPaymentMethodCode().equals("C000")){
            payment.setApprovalNum("0XASDWU123");
        }

        User user = userRepo.findById(paymentDTO.getUserNum()).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ paymentDTO.getUserNum() +"인 사용자가 없습니다.");
        });

        Ticket ticket = ticketRepo.findById(paymentDTO.getTicketNum()).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ paymentDTO.getTicketNum() +"인 티켓이 없습니다.");
        });
        
        if(ticket.getIssued().equals("Y")){
            throw new ResourceNotFoundException("이미 발행된 티켓입니다.");
        }else{
            ticket.setIssued("Y");
            ticketRepo.save(ticket);
        }

        PaymentMethod pm = paymentMethodRepo.findById(paymentDTO.getPaymentMethodCode()).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ paymentDTO.getPaymentMethodCode() +"인 결제 방법이 없습니다.");
        });

        payment.setUser(user);
        payment.setTicket(ticket);
        payment.setPaymentMethod(pm);

        Payment savedPayment = paymentRepo.save(payment);

        return savedPayment;
    }
}
