package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uos.cineseoul.entity.Schedule;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.entity.movie.Movie;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface ScheduleSeatRepository extends JpaRepository<ScheduleSeat, Long> {
    // 상영 일정으로 조회
    List<ScheduleSeat> findAllBySchedule(Schedule schedule);
    List<ScheduleSeat> findAllBySeat(Seat seat);

    // 상영 일정 번호로 조회
    @Query("select ss from SCHEDULE_SEAT ss where ss.schedule.schedNum = :schedNum")
    List<ScheduleSeat> findAllBySchedNum(@Param("schedNum")Long schedNum);

    // 영화로 조회
    List<ScheduleSeat> findAllBySchedule_Movie(Movie movie);

    // 상영일정번호 및 좌석번호로 조회
//    @Query("select ss from SCHEDULE_SEAT ss where ss.schedule = :schedule and ss.seat = :seat")
//    Optional<ScheduleSeat> findBySchedAndSeat(@Param("schedule")Schedule schedule ,@Param("seat")Seat seat);
    Optional<ScheduleSeat> findByScheduleAndSeat(Schedule schedule ,Seat seat);

    @Query("select ss from SCHEDULE_SEAT ss where ss.schedule.schedNum = :schedNum and ss.seat.seatNum = :seatNum")
    Optional<ScheduleSeat> findBySchedNumAndSeatNum(@Param("schedNum")Long schedNum ,@Param("seatNum")Long seatNum);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ss from SCHEDULE_SEAT ss where ss.schedule.schedNum = :schedNum and ss.seat.seatNum = :seatNum")
    Optional<ScheduleSeat> findBySchedNumAndSeatNumForUpdate(@Param("schedNum")Long schedNum ,@Param("seatNum")Long seatNum);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ss from SCHEDULE_SEAT ss where ss.scheduleSeatNum = :scheduleSeatNum")
    Optional<ScheduleSeat> findByIdForUpdate(@Param("scheduleSeatNum")Long scheduleSeatNum);
}