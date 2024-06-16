
package uos.cineseoul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
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
    private final TicketSeatRepository ticketSeatRepo;
    private final TicketAudienceRepository ticketAudienceRepo;

    public List<Ticket> findAll() {
        return ticketRepo.findAll();
    }

    public Page<Ticket> findAll(Pageable pageable) {
        return ticketRepo.findAll(pageable);
    }

    public Page<Ticket> findAllByTicketState(TicketState ticketState,Pageable pageable) {
        return ticketRepo.findAllByTicketState(ticketState, pageable);
    }

    public List<Ticket> findByUserNum(Long userNum) {
        return ticketRepo.findByUserNum(userNum);
    }

    public Page<Ticket> findByUserNum(Long userNum, Pageable pageable) {
        return ticketRepo.findByUser_UserNum(userNum, pageable);
    }

    public Page<Ticket> findByUserNumAndTicketState(Long userNum, TicketState ticketState, Pageable pageable) {
        return ticketRepo.findByUser_UserNumAndTicketState(userNum, ticketState, pageable);
    }

    public Ticket findOneByNum(Long num) {
        return ticketRepo.findById(num).orElseThrow(()-> new ResourceNotFoundException("번호가 " + num + "인 티켓이 없습니다."));
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
        if(LocalDateTime.now().isAfter(ticketDTO.getSchedule().getSchedTime().minusMinutes(15))) throw new ForbiddenException("티켓은 상영시간 15분 전까지만 예매할 수 있습니다.");

        Ticket ticket = TicketMapper.INSTANCE.toEntity(ticketDTO);
        Ticket savedTicket = ticketRepo.save(ticket);

        AtomicReference<Integer> price = new AtomicReference<>(0);
        AtomicReference<Integer> discountPrice = new AtomicReference<>(0);

        savedTicket.setTicketSeats(insertTicketSeatList(ticket, seatNumList, price));
        savedTicket.setAudienceTypes(insertTicketAudienceList(ticket, createTicketAudienceDTOList, discountPrice));

        if(!ticket.getStdPrice().equals(price.get()-discountPrice.get())){
            throw new DataInconsistencyException("표준 가격이 좌석 가격에서 할인가격을 뺀 값과 일치하지 않습니다.");
        }

        return savedTicket;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public List<TicketSeat> insertTicketSeatList(Ticket ticket, List<Long> seatNumList, AtomicReference<Integer> price){
        List<TicketSeat> ticketSeatList = new ArrayList<>();
        seatNumList.forEach(seatNum->{
            ScheduleSeat scheduleSeat = scheduleSeatRepo.findBySchedNumAndSeatNum(ticket.getSchedule().getSchedNum(),seatNum).orElseThrow(()-> new ResourceNotFoundException("번호가 " + ticket.getSchedule().getSchedNum() + "인 상영일정에는 번호가" + seatNum + "인 좌석이 없습니다."));
            if(scheduleSeat.getIsOccupied().equals(Is.Y)){
                throw new DuplicateKeyException("해당 상영일정 좌석에 대해 이미 예약이 되어있습니다.");
            }
            // 좌석 할당 처리
            scheduleSeat.setIsOccupied(Is.Y);
            // 버전 관리를 위한 필드
            scheduleSeat.setVersion(scheduleSeat.getVersion() + 1);
            // 동시 예매 처리
            try {
                scheduleSeatRepo.save(scheduleSeat);
            } catch (OptimisticLockingFailureException e) {
                throw new OptimisticLockingFailureException("데이터가 다른 사용자에 의해 수정되었습니다. 다시 시도해주세요.");
            }

            // 티켓-상영일정-좌석 생성
            InsertReservationSeatDTO iReservationSeatDTO = InsertReservationSeatDTO.builder().ticket(ticket).seat(scheduleSeat.getSeat()).build();
            TicketSeat ticketSeat = ReservationSeatMapper.INSTANCE.toEntity(iReservationSeatDTO);
            ticketSeatList.add(ticketSeat);
            // 가격 체크를 위함
            price.updateAndGet(v -> v + scheduleSeat.getSeat().getSeatGrade().getPrice());
            // 상영일정 빈자리 수 1개 내리기
            Schedule schedule = ticket.getSchedule();
            schedule.setEmptySeat(schedule.getEmptySeat()-1);
            scheduleRepo.save(schedule);
            // 영화의 예매 수 1개 올리기
            Movie movie = schedule.getMovie();
            movie.setReservationCount(movie.getReservationCount()+1);
            movieRepo.save(movie);
        });

        ticketSeatRepo.saveAll(ticketSeatList);

        return ticketSeatList;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public List<Audience> insertTicketAudienceList(Ticket ticket, List<CreateTicketAudienceDTO> createTicketAudienceDTOList, AtomicReference<Integer> discountPrice){
        List<Audience> audienceList = new ArrayList<>();
        createTicketAudienceDTOList.forEach(createTicketAudienceDTO -> {
            Audience audience = Audience.builder().ticket(ticket).audienceType(createTicketAudienceDTO.getAudienceType()).count(createTicketAudienceDTO.getCount()).build();
            audienceList.add(audience);
            // 할인 가격 체크를 위함
            discountPrice.updateAndGet(v -> v + audience.getAudienceType().getDiscountPrice() * audience.getCount());
        });
        ticketAudienceRepo.saveAll(audienceList);
        return audienceList;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Ticket update(Long ticketNum, UpdateTicketDTO ticketDTO) {
        Ticket ticket = findOneByNum(ticketNum);
        // 이미 취소된 티켓인지 확인
        if(ticket.getTicketState().equals(TicketState.C)){
            throw new ForbiddenException("이미 취소된 티켓입니다.");
        }

        // 티켓 취소 처리
        if(ticketDTO.getTicketState().equals(TicketState.C)){
            if(LocalDateTime.now().isAfter(ticket.getSchedule().getSchedTime().minusMinutes(10))) throw new ForbiddenException("티켓은 상영 10분 전까지만 취소할 수 있습니다.");
            cancelProcess(ticket);
        }

        // 판매가격 처리
        if(ticketDTO.getSalePrice()!=null && ticketDTO.getSalePrice() > ticket.getStdPrice()){
            throw new DataInconsistencyException("표준 가격보다 판매 가격이 더 높습니다.");
        }

        TicketMapper.INSTANCE.updateFromDto(ticketDTO,ticket);

        return ticketRepo.save(ticket);
    }

    // 티켓 삭제
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void deleteByNum(Long ticketNum) {
        Ticket ticket = findOneByNum(ticketNum);
        if(ticket.getTicketState().equals(TicketState.P) || ticket.getTicketState().equals(TicketState.I)) throw new ForbiddenException("이미 결제된 티켓은 임의로 지울 수 없습니다.");
        checkStateAndRefund(ticket,ticket.getUser());
        delete(ticket, !ticket.getTicketState().equals(TicketState.C));
    }

    // 티켓 취소
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void cancelByNum(Long ticketNum) {
        Ticket ticket = findOneByNum(ticketNum);
        cancelProcess(ticket);
        ticketRepo.save(ticket);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void delete(Ticket ticket, boolean isEditScheduleSeat) {
        if(isEditScheduleSeat)
            editScheduleSeats(ticket.getTicketSeats(), ticket.getSchedule());
        ticketSeatRepo.deleteAll(ticket.getTicketSeats());
        ticketAudienceRepo.deleteAll(ticket.getAudienceTypes());
        paymentRepo.findByTicket(ticket).ifPresent(paymentRepo::delete);
        ticketRepo.delete(ticket);

        if(ticket.getUser().getRole().equals(UserRole.N) && (ticketRepo.findByUserNum(ticket.getUser().getUserNum()).isEmpty())){
            userRepo.deleteById(ticket.getUser().getUserNum());
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void checkUser(Long ticketNum, Long userNum){
        if(!findOneByNum(ticketNum).getUser().getUserNum().equals(userNum))
            throw new ForbiddenException("티켓 예매자와 현 사용자의 정보가 일치하지 않습니다.");
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void cancelProcess(Ticket ticket) {
        // 환불
        checkStateAndRefund(ticket,ticket.getUser());
        editScheduleSeats(ticket.getTicketSeats(),ticket.getSchedule());
        ticket.setCanceledAt(LocalDateTime.now());
        ticket.setTicketState(TicketState.C);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void editScheduleSeats(List<TicketSeat> ticketSeatList, Schedule schedule) {
        // 상영일정-좌석 수정
        ticketSeatList.forEach(reservationSeat -> {
            ScheduleSeat scheduleSeat = scheduleSeatRepo.findByScheduleAndSeat(schedule,reservationSeat.getSeat()).orElse(null);
            if(scheduleSeat!=null){
                scheduleSeat.setIsOccupied(Is.N);
                scheduleSeatRepo.save(scheduleSeat);
            }
            // 상영일정 빈자리 수 1개 올리기
            schedule.setEmptySeat(Math.min(schedule.getEmptySeat()+1,schedule.getScreen().getTotalSeat()));
            scheduleRepo.save(schedule);
            // 영화의 예매 수 1개 내리기
            Movie movie = schedule.getMovie();
            movie.setReservationCount(Math.max(movie.getReservationCount()-1,0));
            movieRepo.save(movie);
        });
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Ticket cancelAndChangeSeat(Long ticketNum,InsertTicketDTO insertTicketDTO, UpdateTicketDTO updateTicketDTO, List<Long> seatNumList, List<CreateTicketAudienceDTO> createTicketAudienceDTOList) {
        // 티켓 예매자 확인
        checkUser(ticketNum, insertTicketDTO.getUser().getUserNum());
        // 티켓 취소
        if(insertTicketDTO.getUser().getRole().equals(UserRole.N)){
            cancelByNum(ticketNum);
        }else{
            update(ticketNum, updateTicketDTO);
        }
        return insert(insertTicketDTO, seatNumList, createTicketAudienceDTOList);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void checkStateAndRefund(Ticket ticket, User user){
        if(!ticket.getTicketState().equals(TicketState.P)) return;
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
                    break;
            }
            if(!user.getRole().equals(UserRole.N)){
                // 결제 포인트 환불
                user.setPoint(user.getPoint()+payment.getPayedPoint());
                // 결제해서 얻은 포인트 반환
                user.setPoint(user.getPoint()-(int)(payment.getPrice()*0.05));
                userRepo.save(user);
            }
            // 결제 취소 상태로 변경
            payment.setState(PayState.C);
            payment.setCanceledAt(LocalDateTime.now());
            paymentRepo.save(payment);
        }
    }

    public static PrintTicketDTO toPrintDTO(Ticket ticket){
        PrintTicketDTO ticketDTO = TicketMapper.INSTANCE.toDTO(ticket);
        ticketDTO.setByTicket(ticket);
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
