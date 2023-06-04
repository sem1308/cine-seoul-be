package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.*;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}