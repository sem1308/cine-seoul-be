package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uos.cineseoul.entity.Schedule;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.Screen;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 날짜별 상영 일정 조회
    //@Query("select s from SCHEDULE s where s.schedTime between :start and :end")
    List<Schedule> findAllBySchedTimeBetween(LocalDateTime start, LocalDateTime end);

    // 날짜, 상영관 번호로 일정 조회
    @Query("select s from SCHEDULE s where schedTime = :schedTime and s.screen.screenNum = :screenNum")
    Optional<Schedule> findBySchedTimeAndScreenNum(@Param("schedTime")LocalDateTime schedTime, @Param("screenNum")Long screenNum);

    // 영화별 상영 일정 조회
    // - 영화 번호로 조회
//    @Query("select s from Schedule s where s.movie.movieNum = :movieNum")
//    List<Schedule> findByMovieNumAndDateBetween(@Param("movieNum") String movieNum);

    // - 영화 제목으로 조회
//    @Query("select s from Schedule s where s.movie.movieName = :movieName")
//    List<Schedule> findByMovieNameAndDateBetween(@Param("movieName") String movieName);

    // 영화(+날짜)별 상영 일정 조회
    // @Query("select s from Schedule s where s.movie.movieName = :movieName and s.schedTime between :start and :end")
//    List<Schedule> findByMovieAndDateBetween(LocalDateTime start, LocalDateTime end, @Param("movieName") String movieName);

}