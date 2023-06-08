package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uos.cineseoul.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.startAt <= :localDateTime and e.endAt >= :localDateTime")
    List<Event> findAllByNow(LocalDateTime localDateTime);
}
