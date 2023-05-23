package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uos.cineseoul.entity.Payment;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.entity.User;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("select p from PAYMENT p where p.user.id = :userId")
    List<Payment> findByUserId(@Param("userId") String userId);

    List<Payment> findByUser(User user);

    @Query("select p from PAYMENT p where p.user.userNum = :userNum")
    List<Payment> findByUserNum(@Param("userNum") Long userNum);

    Optional<Payment> findByTicket(Ticket ticket);
    @Query("select p from PAYMENT p where p.ticket.ticketNum = :ticketNum")
    Optional<Payment> findByTicketNum(@Param("ticketNum") Long ticketNum);
}