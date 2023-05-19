package uos.cineseoul.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uos.cineseoul.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    //@Query("select t from TICKET t where t.USER_NUM = :UserNum")
    List<Ticket> findByUser(@Param("UserNum") Long UserNum);

    @Query("select t from USERS u, TICKET t where u = t.user and u.id = :userID")
    List<Ticket> findByUserID(@Param("userID") String userID);

    //@Query("select t from TICKET t where t.USER_NUM = :userNum")
    Optional<Ticket> findByUserAndTicketNum(@Param("userNum") Long userNum, @Param("ticketNum") Long ticketNum);

    @Query("select t from USERS u, TICKET t where u = t.user and u.id = :userID and t.ticketNum = :ticketNum")
    Optional<Ticket> findByUserIDAndTicketNum(@Param("userID") String userID, @Param("ticketNum") Long ticketNum);

}