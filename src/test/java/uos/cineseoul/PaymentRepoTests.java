package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.entity.*;
import uos.cineseoul.repository.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Slf4j
class PaymentRepoTests {
	@Autowired
	PaymentRepository paymentRepo;
	@Autowired
	PaymentMethodRepository paymentMethodRepo;
	@Autowired
	TicketRepository ticketRepo;
	@Autowired
	UserRepository userRepo;
	@Test
	@Transactional
	void paymentTest() {
		Integer price = 7500;
		LocalDateTime createdAt = LocalDateTime.now();

		String userID = "sem1308";
		User user = userRepo.findByUserId(userID).get();
		Long ticketNum = 2L;
		Ticket ticket = ticketRepo.findById(ticketNum).get();

		String paymentMethodCode = "C000";

		PaymentMethod pm = paymentMethodRepo.findById(paymentMethodCode).orElse(null);

		Payment payment = Payment.builder().ticket(ticket).approvalNum("0XASDWU123")
				.createdAt(createdAt).price(price).user(user).paymentMethod(pm).build();

		Payment savedPayment = paymentRepo.save(payment);
	}
	@Test
	void findOneTest() {
		Long ticketNum = 2L;
		Payment payment = paymentRepo.findByTicketNum(ticketNum).get();

		assert payment.getTicket().getTicketNum().equals(ticketNum);
	}

	@Test
	void findAllTest() {
		Long userNum = 1L;
		String userId = "sem1308";
		// by userNum
		List<Payment> paymentList1 = paymentRepo.findByUserNum(userNum);

		// by userId
		List<Payment> paymentList2 = paymentRepo.findByUserId(userId);
	}
}
