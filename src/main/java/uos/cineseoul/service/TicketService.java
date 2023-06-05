
package uos.cineseoul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.create.CreateTicketAudienceDTO;
import uos.cineseoul.dto.insert.InsertReservationSeatDTO;
import uos.cineseoul.dto.insert.InsertTicketDTO;
import uos.cineseoul.dto.response.PrintTicketDTO;
import uos.cineseoul.dto.update.UpdateTicketDTO;
import uos.cineseoul.entity.*;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.exception.DataInconsistencyException;
import uos.cineseoul.exception.ForbiddenException;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.TicketMapper;
import uos.cineseoul.mapper.ReservationSeatMapper;
import uos.cineseoul.repository.*;
import uos.cineseoul.utils.enums.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
    private final ReservationSeatRepository reservationSeatRepo;
    private final TicketAudienceRepository ticketAudienceRepo;

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
    public Ticket insert(InsertTicketDTO ticketDTO, List<Long> seatNumList, List<CreateTicketAudienceDTO> createTicketAudienceDTOList) {
        AtomicReference<Integer> audienceCount = new AtomicReference<>(0);
        createTicketAudienceDTOList.forEach(audienceDTO->{
            audienceCount.updateAndGet(v -> v + audienceDTO.getCount());
        });
        if(!audienceCount.get().equals(seatNumList.size())){
            throw new DataInconsistencyException("좌석 개수와 관객 총 개수가 다릅니다.");
        }

        Ticket ticket = TicketMapper.INSTANCE.toEntity(ticketDTO);

        Ticket savedTicket = ticketRepo.save(ticket);
        List<ReservationSeat> reservationSeatList = insertReservationSeatList(ticket, seatNumList);
        List<TicketAudience> ticketAudienceList = insertTicketAudienceList(ticket, createTicketAudienceDTOList);
        savedTicket.setReservationSeats(reservationSeatList);
        savedTicket.setAudienceTypes(ticketAudienceList);
        return savedTicket;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public List<ReservationSeat> insertReservationSeatList(Ticket ticket, List<Long> seatNumList){
        AtomicReference<Integer> price = new AtomicReference<>(0);
        List<ReservationSeat> reservationSeatList = new ArrayList<>();
        // TODO: 이렇게 해도 동시에 예매하는게 해결될지?
        seatNumList.forEach(seatNum->{
            ScheduleSeat scheduleSeat = scheduleSeatRepo.findBySchedNumAndSeatNum(ticket.getSchedule().getSchedNum(),seatNum).orElseThrow(()->{
                throw new ResourceNotFoundException("번호가 " + ticket.getSchedule().getSchedNum() + "인 상영일정에는 번호가" + seatNum + "인 좌석이 없습니다.");
            });
            if(scheduleSeat.getIsOccupied().equals(Is.Y)){
                throw new DuplicateKeyException("해당 상영일정 좌석에 대해 이미 예약이 되어있습니다.");
            }
            // 좌석 할당 처리
            scheduleSeat.setIsOccupied(Is.Y);
            scheduleSeatRepo.save(scheduleSeat);
            // 티켓-상영일정-좌석 생성
            InsertReservationSeatDTO iReservationSeatDTO = InsertReservationSeatDTO.builder().ticket(ticket).seat(scheduleSeat.getSeat()).build();
            ReservationSeat reservationSeat = ReservationSeatMapper.INSTANCE.toEntity(iReservationSeatDTO);
            reservationSeatRepo.save(reservationSeat);
            reservationSeatList.add(reservationSeat);
            // 가격 체크를 위함
            price.updateAndGet(v -> v + scheduleSeat.getSeat().getSeatGrade().getPrice());
            // TODO: 예매 자리수로 할지 예매 티켓 수로 할지
            // 상영일정 빈자리 수 1개 내리기
            Schedule schedule = ticket.getSchedule();
            schedule.setEmptySeat(schedule.getEmptySeat()-1);
            scheduleRepo.save(schedule);
            // 영화의 예매 수 1개 올리기
            Movie movie = schedule.getMovie();
            movie.setTicketCount(movie.getTicketCount()+1);
            movieRepo.save(movie);
        });

        // TODO: 관객 타입에 따른 가격 변화
        if(!price.get().equals(ticket.getStdPrice())){
            throw new DataInconsistencyException("표준 가격이 좌석 가격합과 일치하지 않습니다.");
        }

        return reservationSeatList;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public List<TicketAudience> insertTicketAudienceList(Ticket ticket, List<CreateTicketAudienceDTO> createTicketAudienceDTOList){
        List<TicketAudience> ticketAudienceList = new ArrayList<>();
        createTicketAudienceDTOList.forEach(createTicketAudienceDTO -> {
            TicketAudience ticketAudience = TicketAudience.builder().ticket(ticket).audienceType(createTicketAudienceDTO.getAudienceType()).count(createTicketAudienceDTO.getCount()).build();
            ticketAudienceRepo.save(ticketAudience);
            ticketAudienceList.add(ticketAudience);
        });
        return ticketAudienceList;
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
            cancelProcess(ticket,ticket.getUser().getRole());
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
        cancelProcess(ticket,userRole);
        ticketRepo.delete(ticket);

        if(user.getTickets().size()==1){
            userRepo.deleteById(user.getUserNum());
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void checkUser(Long ticketNum, Long userNum){
        if(!findOneByNum(ticketNum).getUser().getUserNum().equals(userNum))
            throw new ForbiddenException("티켓 예매자와 현 사용자의 정보가 일치하지 않습니다.");
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    private void cancelProcess(Ticket ticket, UserRole userRole) {
        // 환불
        checkStateAndRefund(ticket,userRole);
        // 상영일정-좌석 수정
        ticket.getReservationSeats().forEach(reservationSeat -> {
            ScheduleSeat scheduleSeat = scheduleSeatRepo.findByScheduleAndSeat(ticket.getSchedule(),reservationSeat.getSeat()).get();
            scheduleSeat.setIsOccupied(Is.N);
            scheduleSeatRepo.save(scheduleSeat);
            // 상영일정 빈자리 수 1개 올리기
            Schedule schedule = scheduleSeat.getSchedule();
            schedule.setEmptySeat(schedule.getEmptySeat()+1);
            scheduleRepo.save(schedule);
            // 영화의 예매 수 1개 내리기
            Movie movie = schedule.getMovie();
            movie.setTicketCount(movie.getTicketCount()-1);
            movieRepo.save(movie);
        });
        ticket.setCanceledAt(LocalDateTime.now());
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Ticket cancelAndChangeSeat(Long ticketNum,InsertTicketDTO insertTicketDTO, UpdateTicketDTO updateTicketDTO, List<Long> seatNumList, List<CreateTicketAudienceDTO> createTicketAudienceDTOList) {
        // 티켓 예매자 확인
        checkUser(ticketNum, insertTicketDTO.getUser().getUserNum());
        // 티켓 취소
        if(insertTicketDTO.getUser().getRole().equals(UserRole.N)){
            delete(ticketNum);
        }else{
            update(ticketNum, updateTicketDTO);
        }
        Ticket ticket = insert(insertTicketDTO, seatNumList, createTicketAudienceDTOList);
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
                payment.setCanceledAt(LocalDateTime.now());
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
