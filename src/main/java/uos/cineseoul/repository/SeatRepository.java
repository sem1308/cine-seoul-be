package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}