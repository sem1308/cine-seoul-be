
package uos.cineseoul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertTicketDTO;
import uos.cineseoul.dto.insert.InsertReservationDTO;
import uos.cineseoul.dto.response.PrintTicketDTO;
import uos.cineseoul.dto.update.UpdateTicketDTO;
import uos.cineseoul.entity.*;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.exception.DataInconsistencyException;
import uos.cineseoul.exception.ForbiddenException;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.TicketMapper;
import uos.cineseoul.mapper.ReservationMapper;
import uos.cineseoul.repository.*;
import uos.cineseoul.utils.enums.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepo;
    private final ScheduleRepository scheduleRepo;
    private final ScheduleSeatRepository scheduleSeatRepo;
    private final AccountService accountService;
    private final PaymentRepository paymentRepo;
    private final MovieRepository movieRepo;
    private final UserRepository userRepo;
    private final ReservationRepository reservationRepo;

    public List<Ticket> findAll() {
        List<Ticket> ticketList = ticketRepo.findAll();
        return ticketList;
    }

    public Page<Ticket> findAll(Pageable pageable) {
        Page<Ticket> ticketList = ticketRepo.findAll(pageable);
        return ticketList;
    }

    public List<Ticket> findByUserNum(Long userNum) {
        List<Ticket> ticketList = ticketRepo.findByUserNum(userNum);
        return ticketList;
    }

    public Page<Ticket> findByUserNum(Long userNum, Pageable pageable) {
        Page<Ticket> ticketList = ticketRepo.findByUser_UserNum(userNum, pageable);
        return ticketList;
    }

    public Ticket findOneByNum(Long num) {
        Ticket ticket = ticketRepo.findById(num).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ num +"인 티켓이 없습니다.");
        });
        return ticket;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Ticket insert(InsertTicketDTO ticketDTO, List<InsertReservationDTO> iTicketScheduleSeatDTOS) {
        Ticket ticket = TicketMapper.INSTANCE.toEntity(ticketDTO);

        Ticket savedTicket = ticketRepo.save(ticket);

        savedTicket.setReservations(insertReservationList(ticket, iTicketScheduleSeatDTOS));
        return savedTicket;
    }

    public List<Reservation> insertReservationList(Ticket ticket, List<InsertReservationDTO> iTicketScheduleSeatDTOS){
        List<Reservation> reservations = new ArrayList<>();
        // TODO: 이렇게 해도 동시에 예매하는게 해결될지?
        iTicketScheduleSeatDTOS.forEach(iTicketScheduleSeatDTO -> {
            ScheduleSeat scheduleSeat = iTicketScheduleSeatDTO.getScheduleSeat();
            if(scheduleSeat.getIsOccupied().equals(Is.Y)){
                throw new DuplicateKeyException("해당 상영일정 좌석에 대해 이미 예약이 되어있습니다.");
            }
            // 좌석 할당 처리
            scheduleSeat.setIsOccupied(Is.Y);
            scheduleSeatRepo.save(scheduleSeat);
            // 티켓-상영일정-좌석 생성
            iTicketScheduleSeatDTO.setTicket(ticket);
            Reservation reservation = ReservationMapper.INSTANCE.toEntity(iTicketScheduleSeatDTO);
            reservations.add(reservation);
            reservationRepo.save(reservation);
            // TODO: 예매 자리수로 할지 예매 티켓 수로 할지
            // 상영일정 빈자리 수 1개 내리기
            Schedule schedule = scheduleSeat.getSchedule();
            schedule.setEmptySeat(schedule.getEmptySeat()-1);
            scheduleRepo.save(schedule);
            // 영화의 예매 수 1개 올리기
            Movie movie = schedule.getMovie();
            movie.setTicketCount(movie.getTicketCount()+1);
            movieRepo.save(movie);
        });
        return reservations;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Ticket update(Long ticketNum, UpdateTicketDTO ticketDTO) {
        Ticket ticket = findOneByNum(ticketNum);
        // 이미 취소된 티켓인지 확인
        if(ticket.getTicketState().equals(TicketState.C)){
            throw new ForbiddenException("이미 취소된 티켓입니다.");
        }

        // 판매가격 처리
        if(ticketDTO.getSalePrice()!=null && ticketDTO.getSalePrice() > ticket.getStdPrice()){
            throw new DataInconsistencyException("표준 가격보다 판매 가격이 더 높습니다.");
        }

        TicketMapper.INSTANCE.updateFromDto(ticketDTO,ticket);

        // 티켓 취소 처리
        if(ticket.getTicketState().equals(TicketState.C)){
            cancelProcess(ticket,ticket.getUser().getRole(), false);
        }

        Ticket updatedTicket = ticketRepo.save(ticket);

        return updatedTicket;
    }

    // 비회원 티켓 삭제
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void delete(Long ticketNum) {
        Ticket ticket = findOneByNum(ticketNum);

        User user = ticket.getUser();
        UserRole userRole = user.getRole();
        if(!userRole.equals(UserRole.N)){
            throw new ForbiddenException("비회원 티켓이 아니면 삭제하지 못합니다.");
        }
        // TODO: 비회원 예약도 삭제할지
        cancelProcess(ticket,userRole, true);
        ticketRepo.delete(ticket);

        if(user.getTickets().size()==1){
            userRepo.deleteById(user.getUserNum());
        }
    }

    public void checkUser(Long ticketNum, Long userNum){
        if(!findOneByNum(ticketNum).getUser().getUserNum().equals(userNum))
            throw new ForbiddenException("티켓 예매자와 현 사용자의 정보가 일치하지 않습니다.");
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    private void cancelProcess(Ticket ticket, UserRole userRole, boolean isDelete) {
        // 환불
        checkStateAndRefund(ticket,userRole);
        // 상영일정-좌석 수정
        ticket.getReservations().forEach(reservation -> {
            ScheduleSeat scheduleSeat = reservation.getScheduleSeat();
            scheduleSeat.setIsOccupied(Is.N);
            scheduleSeatRepo.save(scheduleSeat);
            if(isDelete){
                // 예약 삭제
                reservationRepo.delete(reservation);
            }
            // 상영일정 빈자리 수 1개 올리기
            Schedule schedule = scheduleSeat.getSchedule();
            schedule.setEmptySeat(schedule.getEmptySeat()+1);
            scheduleRepo.save(schedule);
            // 영화의 예매 수 1개 내리기
            Movie movie = schedule.getMovie();
            movie.setTicketCount(movie.getTicketCount()-1);
            movieRepo.save(movie);
        });
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Ticket changeSeat(Long ticketNum, UpdateTicketDTO updateTicketDTO, User user, List<InsertReservationDTO> insertReservationDTOS) {
        Ticket ticket = findOneByNum(ticketNum);
        // 티켓 예매자 확인
        checkUser(ticketNum, user.getUserNum());
        // 티켓 자리 변경
        update(ticketNum, updateTicketDTO);
        cancelProcess(ticket, user.getRole(), true);
        
        List<Reservation> reservations = insertReservationList(ticket,insertReservationDTOS);
        ticket.setReservations(reservations);
        return ticket;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Ticket cancelAndChangeSeat(Long ticketNum, UpdateTicketDTO updateTicketDTO, User user, List<InsertReservationDTO> insertReservationDTOS) {
        InsertTicketDTO insertTicketDTO = InsertTicketDTO.builder().ticketState(TicketState.N).stdPrice(0).user(user).build();
        // 티켓 예매자 확인
        checkUser(ticketNum, insertTicketDTO.getUser().getUserNum());
        // 티켓 취소
        if(insertTicketDTO.getUser().getRole().equals(UserRole.N)){
            delete(ticketNum);
        }else{
            update(ticketNum, updateTicketDTO);
        }
        // 티켓 등록
        Ticket ticket = insert(insertTicketDTO, insertReservationDTOS);
        return ticket;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    private void checkStateAndRefund(Ticket ticket, UserRole role){
        Optional<Payment> p = paymentRepo.findByTicketNum(ticket.getTicketNum());
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
            if(role.equals(UserRole.N)){
                //TODO:비회원도 남길지
                paymentRepo.delete(payment);
            }else{
                // 결제 취소 상태로 변경
                payment.setState(PayState.C);
                paymentRepo.save(payment);
            }
        }
    }

    public static PrintTicketDTO toPrintDTO(Ticket ticket){
        PrintTicketDTO ticketDTO = TicketMapper.INSTANCE.toDTO(ticket);
        ticketDTO.setScheduleAndTicketScheduleSeats(ticket);
        return ticketDTO;
    }

    public static List<PrintTicketDTO> toPrintDTOList(List<Ticket> ticketList){
        List<PrintTicketDTO> pTicketList = new ArrayList<>();
        ticketList.forEach(ticket -> {
            pTicketList.add(toPrintDTO(ticket));
        });
        return pTicketList;
    }

}
