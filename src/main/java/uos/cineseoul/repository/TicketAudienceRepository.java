package uos.cineseoul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.entity.TicketAudience;

import java.util.List;

public interface TicketAudienceRepository extends JpaRepository<TicketAudience, Long> {
}