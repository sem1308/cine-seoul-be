package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.*;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {
}