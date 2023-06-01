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

    /* 날짜별 상영 일정 조회 */
    //@Query("select s from SCHEDULE s where s.schedTime between :start and :end")
    List<Schedule> findAllBySchedTimeBetween(LocalDateTime start, LocalDateTime end);

    // 날짜, 상영관 번호로 일정 조회
    @Query("select s from SCHEDULE s where s.schedTime = :schedTime and s.screen.screenNum = :screenNum")
    Optional<Schedule> findBySchedTimeAndScreenNum(@Param("schedTime")LocalDateTime schedTime, @Param("screenNum")Long screenNum);

    // 상영일정으로 정렬 (오름차순)
    @Query("select s from SCHEDULE s where schedTime = :schedTime and s.screen.screenNum = :screenNum ORDER BY s.schedTime ASC")
    Optional<Schedule> findBySchedTimeAndScreenNumOrderBySchedTimeASC(@Param("schedTime")LocalDateTime schedTime, @Param("screenNum")Long screenNum);

    /* 상영관 번호로 일정 조회 */
    @Query("select s from SCHEDULE s where s.screen.screenNum = :screenNum")
    List<Schedule> findByScreenNum(@Param("screenNum")Long screenNum);


    /* 영화별 상영 일정 조회 */
    // - 영화 번호로 조회
    @Query("select s from SCHEDULE s where s.movie.movieNum = :movieNum")
    List<Schedule> findByMovieNum(@Param("movieNum") Long movieNum);

    // - 영화 제목으로 조회
    @Query("select s from SCHEDULE s where s.movie.title = :title")
    List<Schedule> findByMovieTitle(@Param("title") String title);

    // 영화(+날짜)별 상영 일정 조회
     @Query("select s from SCHEDULE s where s.movie.title = :title and s.schedTime between :start and :end")
    List<Schedule> findByMovieNameAndDateBetween(LocalDateTime start, LocalDateTime end, @Param("title") String title);

     @Query("select s from SCHEDULE s where s.movie.movieNum = :movieNum and s.schedTime between :start and :end")
    List<Schedule> findByMovieNumAndDateBetween(LocalDateTime start, LocalDateTime end, @Param("movieNum") Long movieNum);

}