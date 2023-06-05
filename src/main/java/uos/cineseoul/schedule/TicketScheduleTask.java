package uos.cineseoul.schedule;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.entity.Schedule;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.entity.User;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.*;
import uos.cineseoul.service.AccountService;
import uos.cineseoul.service.TicketService;
import uos.cineseoul.utils.enums.Is;
import uos.cineseoul.utils.enums.TicketState;
import uos.cineseoul.utils.enums.UserRole;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class TicketScheduleTask {
    private final TicketRepository ticketRepo;
    private final ScheduleRepository scheduleRepo;
    private final ScheduleSeatRepository scheduleSeatRepo;
    private final MovieRepository movieRepo;
    private final UserRepository userRepo;
    private final ReservationSeatRepository reservationSeatRepo;
    private final TicketAudienceRepository ticketAudienceRepo;

    @Scheduled(fixedDelay = 900000) // 15분(900,000밀리초)마다 실행
    @Transactional
    public void deleteExpiredTickets() {
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(10);
        List<Ticket> ticketList = ticketRepo.findByTicketStateAndCreatedAtBefore(TicketState.N, dateTime);
        if(!ticketList.isEmpty()){
            ticketList.forEach(ticket -> {
                User user = ticket.getUser();
                delete(ticket);
                if(user.getRole().equals(UserRole.N) && user.getTickets().size()==1){
                    userRepo.deleteById(user.getUserNum());
                }
            });
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    private void delete(Ticket ticket) {
        // 상영일정-좌석 수정
        ticket.getReservationSeats().forEach(reservationSeat -> {
            ScheduleSeat scheduleSeat = scheduleSeatRepo.findByScheduleAndSeat(ticket.getSchedule(),reservationSeat.getSeat()).orElseThrow(()->{
                throw new ResourceNotFoundException("해당 정보에 해당하는 상영일정-좌석이 없습니다.");
            });
            scheduleSeat.setIsOccupied(Is.N);
            scheduleSeatRepo.save(scheduleSeat);
            // 상영일정 빈자리 수 1개 올리기
            Schedule schedule = scheduleSeat.getSchedule();
            schedule.setEmptySeat(Math.min(schedule.getEmptySeat()+1,schedule.getScreen().getTotalSeat()));
            scheduleRepo.save(schedule);
            // 영화의 예매 수 1개 내리기
            Movie movie = schedule.getMovie();
            movie.setTicketCount(Math.max(movie.getTicketCount()-1,0));
            movieRepo.save(movie);
            reservationSeatRepo.delete(reservationSeat);
        });
        ticket.getAudienceTypes().forEach(ticketAudience -> {
            ticketAudienceRepo.delete(ticketAudience);
        });
        ticketRepo.delete(ticket);
    }
}
