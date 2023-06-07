package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
