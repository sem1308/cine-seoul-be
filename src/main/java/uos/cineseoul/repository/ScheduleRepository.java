package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uos.cineseoul.entity.Schedule;
import uos.cineseoul.entity.Screen;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 날짜별 상영 일정 조회
    List<Schedule> findAllBySchedTimeBetween(LocalDateTime start, LocalDateTime end);

//    // 영화별 상영 일정 조회
//    List<Schedule> findByMovieAndDateBetween(LocalDateTime start, LocalDateTime end, @Param("movieName") String movieName);

    // 영화(+날짜)별 상영 일정 조회
}