package uos.cineseoul.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uos.cineseoul.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("select t from TICKET t where t.user.userNum = :userNum")
    List<Ticket> findByUserNum(@Param("userNum") Long userNum);

    @Query("select t from TICKET t where t.user.id = :userID")
    List<Ticket> findByUserID(@Param("userID") String userID);

    @Query("select t from TICKET t where t.user.userNum = :userNum and t.ticketNum = :ticketNum")
    Optional<Ticket> findByUserNumAndTicketNum(@Param("userNum") Long userNum, @Param("ticketNum") Long ticketNum);

    @Query("select t from TICKET t where t.user.id = :userID and t.ticketNum = :ticketNum")
    Optional<Ticket> findByUserIDAndTicketNum(@Param("userID") String userID, @Param("ticketNum") Long ticketNum);
}