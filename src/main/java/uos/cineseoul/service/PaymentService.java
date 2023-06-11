
package uos.cineseoul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertPaymentDTO;
import uos.cineseoul.dto.response.PrintPaymentDTO;
import uos.cineseoul.entity.Payment;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.entity.User;
import uos.cineseoul.exception.DataInconsistencyException;
import uos.cineseoul.exception.ForbiddenException;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.PaymentMapper;
import uos.cineseoul.repository.PaymentRepository;
import uos.cineseoul.repository.TicketRepository;
import uos.cineseoul.repository.UserRepository;
import uos.cineseoul.utils.enums.TicketState;
import uos.cineseoul.utils.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepo;
    private final TicketRepository ticketRepo;
    private final UserRepository userRepo;
    private final AccountService accountService;

    public List<Payment> findAll() {
        List<Payment> paymentList = paymentRepo.findAll();
        if (paymentList.isEmpty()) {
            throw new ResourceNotFoundException("결제 내역이 없습니다.");
        }
        return paymentList;
    }

    public Page<Payment> findAll(Pageable pageable) {
        Page<Payment> paymentList = paymentRepo.findAll(pageable);
        if (paymentList.isEmpty()) {
            throw new ResourceNotFoundException("결제 내역이 없습니다.");
        }
        return paymentList;
    }

    public List<Payment> findByUserNum(Long userNum) {
        List<Payment> paymentList = paymentRepo.findByUserNum(userNum);
        if (paymentList.isEmpty()) {
            throw new ResourceNotFoundException(userNum + "번 유저에 대한 결제 내역이 없습니다.");
        }
        return paymentList;
    }

    public Page<Payment> findByUserNum(Long userNum, Pageable pageable) {
        Page<Payment> paymentList = paymentRepo.findByUserNum(userNum, pageable);
        return paymentList;
    }

    public Payment findOneByNum(Long num) {
        Payment payment = paymentRepo.findById(num).orElseThrow(() -> {
            throw new ResourceNotFoundException("번호가 " + num + "인 결제 내역이 없습니다.");
        });
        return payment;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Payment insert(InsertPaymentDTO paymentDTO) {
        Payment payment = PaymentMapper.INSTANCE.toEntity(paymentDTO);
        Ticket ticket = payment.getTicket();
        User user = payment.getUser();

        // 이미 결제된 티켓인지 확인
        if (ticket.getTicketState().equals(TicketState.P)) {
            throw new ResourceNotFoundException("이미 발행된 티켓입니다.");
        } else {
            ticket.setTicketState(TicketState.P);
        }

        // 결제 가격과 티켓 표준 가격 확인
        if (payment.getPrice() > ticket.getStdPrice()) {
            throw new DataInconsistencyException("결제 가격이 티켓 표쥰 가격보다 높으므로 취소됩니다.");
        }

        // 유저 확인
        if (!user.getUserNum().equals(ticket.getUser().getUserNum())) {
            throw new DataInconsistencyException("티켓 예매한 사용자와 결제하려는 사용자가 다르므로 취소됩니다.");
        }

        switch (payment.getPaymentMethod().toString().charAt(0)) {
            case 'A':
                if(payment.getAccountNum()==null) throw new DataInconsistencyException("계좌결제지만 계좌번호가 입력되지 않았습니다.");
                accountService.payByAccountNum(paymentDTO.getPrice(), paymentDTO.getAccountNum());
                break;
            case 'C':
                if(payment.getCardNum()==null) throw new DataInconsistencyException("카드결제지만 카드번호가 입력되지 않았습니다.");
                accountService.payByCardNum(paymentDTO.getPrice(), user.getName(), paymentDTO.getCardNum());
                accountService.checkVaildity(paymentDTO.getCardNum(), user.getName());
                payment.setApprovalNum(accountService.getApprovalNum(paymentDTO.getCardNum()));
                break;
            case 'P':
                break;
            default:
                throw new ResourceNotFoundException("해당 결제 방법이 존재하지 않습니다.");
        }

        Payment savedPayment = paymentRepo.save(payment);

        ticket.setSalePrice(payment.getPrice());
        ticketRepo.save(ticket);

        // 포인트 결제 사항 체크
        if(!user.getRole().equals(UserRole.N)){
            Integer point = payment.getPayedPoint();
            if(point != null && !point.equals(0)){
                if(user.getPoint() < point) throw new DataInconsistencyException("유저의 포인트가 결제 포인트보다 적습니다.");
                user.setPoint(user.getPoint() - point);
            }
            user.setPoint(user.getPoint()+(int)(payment.getPrice()*0.05));
        }
        userRepo.save(user);

        return savedPayment;
    }

    public PrintPaymentDTO getPrintDTO(Payment payment) {
        PrintPaymentDTO paymentDTO = PaymentMapper.INSTANCE.toDTO(payment);
        paymentDTO.setTicketNum(payment.getTicket().getTicketNum());
        return paymentDTO;
    }

    public List<PrintPaymentDTO> getPrintDTOList(List<Payment> paymentList) {
        List<PrintPaymentDTO> pPaymentList = new ArrayList<>();
        paymentList.forEach(payment -> {
            pPaymentList.add(getPrintDTO(payment));
        });
        return pPaymentList;
    }

}
