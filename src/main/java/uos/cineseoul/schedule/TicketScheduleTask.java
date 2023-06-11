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
    private final TicketService ticketService;

    private void deleteExpiredTickets(List<Ticket> ticketList, boolean isEditScheduleSeat) {
        if(!ticketList.isEmpty()){
            ticketList.forEach(ticket -> {
                ticketService.delete(ticket, isEditScheduleSeat);
            });
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?") // 매 1분 간격으로 결제가 되지 않은 상태이며 생성후 n분 지난 티켓 삭제
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void deleteExpiredTickets() {
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(5); // 테스트를 위해 짧게 설정
        List<Ticket> ticketList = ticketRepo.findByTicketStateAndCreatedAtBefore(TicketState.N, dateTime);
        deleteExpiredTickets(ticketList, true);
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정마다 생성후 1달 지난 티켓 삭제
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void deleteExpiredTicketsByMonth() {
        LocalDateTime dateTime = LocalDateTime.now().minusMonths(1);
        List<Ticket> ticketList = ticketRepo.findByCreatedAtBefore(dateTime);
        deleteExpiredTickets(ticketList, false);
    }
}
