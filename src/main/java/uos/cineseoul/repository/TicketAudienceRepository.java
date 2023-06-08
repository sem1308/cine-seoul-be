package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.Audience;

public interface TicketAudienceRepository extends JpaRepository<Audience, Long> {
}