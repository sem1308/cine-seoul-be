package uos.cineseoul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uos.cineseoul.entity.Schedule;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.utils.enums.TicketState;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("select t from TICKET t where t.user.userNum = :userNum")
    List<Ticket> findByUserNum(@Param("userNum") Long userNum);

    Page<Ticket> findAllByTicketState(TicketState ticketState, Pageable pageable);

    Page<Ticket> findByUser_UserNum(Long userNum, Pageable pageable);
    Page<Ticket> findByUser_UserNumAndTicketState(Long userNum, TicketState ticketState, Pageable pageable);

    @Query("select t from TICKET t where t.user.id = :userID")
    List<Ticket> findByUserID(@Param("userID") String userID);
    List<Ticket> findByTicketStateAndCreatedAtBefore(TicketState ticketState, LocalDateTime createdAt);
    List<Ticket> findByCreatedAtBefore(LocalDateTime createdAt);
    List<Ticket> findBySchedule(Schedule schedule);
    List<Ticket> findByScheduleAndTicketState(Schedule schedule, TicketState ticketState);
    List<Ticket> findByScheduleAndTicketStateNot(Schedule schedule, TicketState ticketState);
}