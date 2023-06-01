
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
import uos.cineseoul.utils.enums.Is;
import uos.cineseoul.utils.enums.PayState;
import uos.cineseoul.utils.enums.TicketState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepo;
    private final ScheduleSeatRepository scheduleSeatRepo;
    private final AccountService accountService;
    private final PaymentRepository paymentRepo;

    @Autowired
    public TicketService(TicketRepository ticketRepo, ScheduleSeatRepository scheduleSeatRepo,
                         AccountService accountService, PaymentRepository paymentRepo) {
        this.ticketRepo = ticketRepo;
        this.scheduleSeatRepo = scheduleSeatRepo;
        this.accountService = accountService;
        this.paymentRepo = paymentRepo;
    }

    public List<Ticket> findAll() {
        List<Ticket> ticketList = ticketRepo.findAll();
        if (ticketList.isEmpty()) {
            throw new ResourceNotFoundException("티켓이 없습니다.");
        }
        return ticketList;
    }

    public Ticket findOneByNum(Long num) {
        Ticket ticket = ticketRepo.findById(num).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ num +"인 티켓이 없습니다.");
        });
        return ticket;
    }
    public List<Ticket> findByUserNum(Long userNum) {
        List<Ticket> ticketList = ticketRepo.findByUserNum(userNum);
        if (ticketList.isEmpty()) {
            throw new ResourceNotFoundException(userNum+"번 유저에 대한 티켓이 없습니다.");
        }
        return ticketList;
    }
    public List<Ticket> findByUserId(String userId) {
        List<Ticket> ticketList = ticketRepo.findByUserID(userId);
        if (ticketList.isEmpty()) {
            throw new ResourceNotFoundException("유저 "+userId+"에 대한 티켓이 없습니다.");
        }
        return ticketList;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Ticket insert(InsertTicketDTO ticketDTO) {
        ScheduleSeat scheduleSeat = ticketDTO.getScheduleSeat();
        if(scheduleSeat.getIsOccupied().equals(Is.Y)){
            throw new DuplicateKeyException("해당 상영일정 좌석에 대해 이미 예약이 되어있습니다.");
        }

        scheduleSeat.setIsOccupied(Is.Y);
        scheduleSeatRepo.save(scheduleSeat);

        Ticket ticket = TicketMapper.INSTANCE.toEntity(ticketDTO);

        // 가격 할당
        assignPrice(ticket);

        Ticket savedTicket = ticketRepo.save(ticket);

        return savedTicket;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Ticket update(Long ticketNum, UpdateTicketDTO ticketDTO) {
        Ticket ticket = findOneByNum(ticketNum);
        // 이미 취소된 티켓인지 확인
        if(ticket.getTicketState().equals(TicketState.C)){
            throw new ForbiddenException("이미 취소된 티켓입니다.");
        }
        
        TicketMapper.INSTANCE.updateFromDto(ticketDTO,ticket);

        Ticket updatedTicket = ticketRepo.save(ticket);

        return updatedTicket;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Ticket changeSeat(Long ticketNum, InsertTicketDTO insertTicketDTO) {
        Ticket ticket = findOneByNum(ticketNum);
        // 이미 취소된 티켓인지 확인
        if(ticket.getTicketState().equals(TicketState.C)){
            throw new ForbiddenException("이미 취소된 티켓입니다.");
        }

        checkIssuedAndRefund(ticket);

        // 새로운 자리의 티켓 생성
        Ticket newTicket = insert(insertTicketDTO);

        return newTicket;
    }

    private void checkIssuedAndRefund(Ticket ticket){
        Optional<Payment> p = paymentRepo.findByTicketNumAndState(ticket.getTicketNum(), PayState.Y);
        if(p.isPresent()){
            Payment payment = p.get();
            // 환불
            switch (payment.getPaymentMethod().toString().charAt(0)){
                case 'A':
                    accountService.refundByAccountNum(payment.getPrice(),payment.getAccountNum());
                    break;
                case 'C':
                    accountService.refundByCardNum(payment.getPrice(), ticket.getUser().getName(),payment.getCardNum());
                    break;
                case 'P':
                    // 포인트 환불
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

    public PrintTicketDTO getPrintDTO(Ticket ticket){
        PrintTicketDTO ticketDTO = TicketMapper.INSTANCE.toDTO(ticket);
        return ticketDTO;
    }

    public List<PrintTicketDTO> getPrintDTOList(List<Ticket> ticketList){
        List<PrintTicketDTO> pTicketList = new ArrayList<>();
        ticketList.forEach(ticket -> {
            pTicketList.add(getPrintDTO(ticket));
        });
        return pTicketList;
    }

}
