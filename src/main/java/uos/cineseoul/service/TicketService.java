
package uos.cineseoul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertTicketDTO;
import uos.cineseoul.dto.response.PrintTicketDTO;
import uos.cineseoul.dto.update.UpdateTicketDTO;
import uos.cineseoul.entity.*;
import uos.cineseoul.exception.DataInconsistencyException;
import uos.cineseoul.exception.ForbiddenException;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.TicketMapper;
import uos.cineseoul.repository.*;
import uos.cineseoul.utils.enums.PayState;
import uos.cineseoul.utils.enums.TicketState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepo;
    private final UserRepository userRepo;
    private final ScheduleSeatRepository scheduleSeatRepo;
    private final AccountService accountService;
    private final PaymentRepository paymentRepo;

    @Autowired
    public TicketService(TicketRepository ticketRepo, UserRepository userRepo, ScheduleSeatRepository scheduleSeatRepo, AccountService accountService, PaymentRepository paymentRepo) {
        this.ticketRepo = ticketRepo;
        this.userRepo = userRepo;
        this.scheduleSeatRepo = scheduleSeatRepo;
        this.accountService = accountService;
        this.paymentRepo = paymentRepo;
    }

    public List<PrintTicketDTO> findAll() {
        List<Ticket> ticketList = ticketRepo.findAll();
        if (ticketList.isEmpty()) {
            throw new ResourceNotFoundException("티켓이 없습니다.");
        }
        return getPrintDTOList(ticketList);
    }

    public PrintTicketDTO findOneByNum(Long num) {
        Ticket ticket = ticketRepo.findById(num).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ num +"인 티켓이 없습니다.");
        });
        return getPrintDTO(ticket);
    }
    public List<PrintTicketDTO> findByUserNum(Long userNum) {
        List<Ticket> ticketList = ticketRepo.findByUserNum(userNum);
        if (ticketList.isEmpty()) {
            throw new ResourceNotFoundException(userNum+"번 유저에 대한 티켓이 없습니다.");
        }
        return getPrintDTOList(ticketList);
    }
    public List<PrintTicketDTO> findByUserId(String userId) {
        List<Ticket> ticketList = ticketRepo.findByUserID(userId);
        if (ticketList.isEmpty()) {
            throw new ResourceNotFoundException("유저 "+userId+"에 대한 티켓이 없습니다.");
        }
        return getPrintDTOList(ticketList);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public PrintTicketDTO insert(InsertTicketDTO ticketDTO) {
        User user = userRepo.findById(ticketDTO.getUserNum()).orElseThrow(()->{
            throw new ResourceNotFoundException(ticketDTO.getUserNum()+"번 유저가 없습니다.");
        });

        ScheduleSeat scheduleSeat = scheduleSeatRepo.findBySchedNumAndSeatNum(ticketDTO.getSchedNum(),ticketDTO.getSeatNum()).orElseThrow(()->{
            throw new ResourceNotFoundException("상영일정 "+ticketDTO.getSchedNum()+"또는 좌석 "+ticketDTO.getSeatNum()+"이 없습니다.");
        });

        if(scheduleSeat.getOccupied().equals("Y")){
            throw new DuplicateKeyException("상영일정 "+ticketDTO.getSchedNum()+", 좌석 "+ticketDTO.getSeatNum()+"에 대해 이미 예약 되어있습니다.");
        }

        scheduleSeat.setOccupied("Y");
        scheduleSeatRepo.save(scheduleSeat);

        Ticket ticket = TicketMapper.INSTANCE.toEntity(ticketDTO);
        ticket.setUser(user);
        ticket.setScheduleSeat(scheduleSeat);

        // 가격 할당
        assignPrice(ticket);

        Ticket savedTicket = ticketRepo.save(ticket);

        return getPrintDTO(savedTicket);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public PrintTicketDTO update(UpdateTicketDTO ticketDTO) {
        Ticket ticket = ticketRepo.findById(ticketDTO.getTicketNum()).orElseThrow(()->{
            throw new ResourceNotFoundException(ticketDTO.getTicketNum()+"번 티켓이 없습니다.");
        });
        // 이미 취소된 티켓인지 확인
        if(ticket.getIssued().equals(TicketState.C)){
            throw new ForbiddenException("이미 취소된 티켓입니다.");
        }
        
        TicketMapper.INSTANCE.updateFromDto(ticketDTO,ticket);

        if(ticket.getIssued().equals(TicketState.C)){
            // 예매 취소
            // 이전 티켓이 이미 결제된 상태라면 환불
            checkIssuedAndRefund(ticket);
        }else{
            // 자리 변경
            if(((ticketDTO.getSchedNum() != null) && !ticketDTO.getSchedNum().equals(ticket.getScheduleSeat().getSchedule().getSchedNum())) ||
                ((ticketDTO.getSeatNum() != null) && !ticketDTO.getSeatNum().equals(ticket.getScheduleSeat().getSeat().getSeatNum()))){
                ScheduleSeat ssBefore = ticket.getScheduleSeat();
                // 바꾸려는 자리가 존재하지 않은 경우 처리
                ScheduleSeat ssAfter = scheduleSeatRepo.findBySchedNumAndSeatNum(ticketDTO.getSchedNum(),ticketDTO.getSeatNum()).orElseThrow(()->{
                    throw new ResourceNotFoundException("상영일정 "+ticketDTO.getSchedNum()+"또는 좌석 "+ticketDTO.getSeatNum()+"이 없습니다.");
                });

                // 바꾸려는 자리가 비어있지 않은 경우 처리
                if(ssAfter.getOccupied().equals("Y")){
                    throw new ResourceNotFoundException("상영일정 "+ticketDTO.getSchedNum()+"의 좌석 "+ticketDTO.getSeatNum()+"은 이미 예약된 상태입니다.");
                }

                // 이전 티켓이 이미 결제된 상태라면 환불
                checkIssuedAndRefund(ticket);

                // 상영일정-좌석 빈자리 여부 변경
                ssBefore.setOccupied("N");
                ssBefore.getSchedule().setEmptySeat(ssBefore.getSchedule().getEmptySeat()+1);
                ssAfter.setOccupied("Y");
                ssAfter.getSchedule().setEmptySeat(ssAfter.getSchedule().getEmptySeat()-1);
                scheduleSeatRepo.save(ssBefore);
                scheduleSeatRepo.save(ssAfter);

                ticket.setScheduleSeat(ssAfter);
            }
            // 가격 할당
            assignPrice(ticket);
            ticket.setIssued(TicketState.N);
        }

        Ticket updatedTicket = ticketRepo.save(ticket);

        return getPrintDTO(updatedTicket);
    }

    private void checkIssuedAndRefund(Ticket ticket){
        Optional<Payment> p = paymentRepo.findByTicketNumAndState(ticket.getTicketNum(), PayState.Y);
        if(p.isPresent()){
            Payment payment = p.get();
            // 환불
            switch (payment.getPaymentMethod().getPaymentMethodCode()){
                case A000:
                    accountService.refundByAccountNum(payment.getPrice(),payment.getAccountNum());
                    break;
                case C000:
                    accountService.refundByCardNum(payment.getPrice(), ticket.getUser().getName(),payment.getCardNum());
                    break;
            }
            // 결제 취소 상태로 변경
            payment.setState(PayState.C);
            paymentRepo.save(payment);
        }
    }

    private void assignPrice(Ticket ticket){
        Integer price = ticket.getScheduleSeat().getSeat().getSeatGrade().getPrice();
        if(ticket.getSalePrice() > price){
            throw new DataInconsistencyException("좌석 가격보다 판매 가격이 더 높습니다.");
        }
        ticket.setStdPrice(price);
    }

    private PrintTicketDTO getPrintDTO(Ticket ticket){
        PrintTicketDTO ticketDTO = TicketMapper.INSTANCE.toDTO(ticket);
        ticketDTO.getScheduleSeat().getSchedule().getScreen().setSeats(null);
        return ticketDTO;
    }

    private List<PrintTicketDTO> getPrintDTOList(List<Ticket> ticketList){
        List<PrintTicketDTO> pTicketList = new ArrayList<>();
        ticketList.forEach(ticket -> {
            pTicketList.add(getPrintDTO(ticket));
        });
        return pTicketList;
    }

}
