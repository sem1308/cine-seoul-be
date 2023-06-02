package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uos.cineseoul.entity.Seat;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Query("select s from SEAT s where s.screen.screenNum = :screenNum and row = :row and col = :col")
    Optional<Seat> findByScreenNumAndRowAndCol(@Param("screenNum") Long screenNum, @Param("row") String row, @Param("col") String col);

    @Query("select s from SEAT s where s.screen.screenNum = :screenNum")
    List<Seat> findByScreenNum(@Param("screenNum") Long screenNum);
}