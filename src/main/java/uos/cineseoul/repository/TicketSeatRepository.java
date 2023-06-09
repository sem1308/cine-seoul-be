package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import uos.cineseoul.entity.*;

import java.util.List;
import java.util.Optional;

public interface TicketSeatRepository extends JpaRepository<TicketSeat, TicketSeatId> {
    Optional<TicketSeat> findBySeat(Seat seat);
}