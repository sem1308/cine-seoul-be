package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uos.cineseoul.entity.Schedule;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.entity.TicketScheduleSeat;

import java.util.List;
import java.util.Optional;

public interface TicketScheduleSeatRepository extends JpaRepository<TicketScheduleSeat, Long> {
}