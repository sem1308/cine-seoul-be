package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.dto.InsertPaymentDTO;
import uos.cineseoul.entity.Payment;
import uos.cineseoul.entity.PaymentMethod;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.entity.User;
import uos.cineseoul.mapper.PaymentMapper;
import uos.cineseoul.repository.PaymentMethodRepository;
import uos.cineseoul.repository.PaymentRepository;
import uos.cineseoul.repository.TicketRepository;
import uos.cineseoul.repository.UserRepository;
import uos.cineseoul.service.PaymentService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Slf4j
class PaymentServiceTests {
	@Autowired
	PaymentService paymentService;

	@Test
	@Transactional
	void paymentTestByMapper() {
		Integer price = 7500;

		Long userNum = 1L;
		Long ticketNum = 1L;
		String paymentMethodCode = "C000";

		InsertPaymentDTO paymentDTO = InsertPaymentDTO.builder().ticketNum(ticketNum)
				.price(price).userNum(userNum).paymentMethodCode(paymentMethodCode).build();

		Payment savedPayment = paymentService.insert(paymentDTO);
	}

	@Test
	void findOneTest() {
		Long paymentNum = 1L;
		Payment payment = paymentService.findOneByNum(paymentNum);

		assert payment.getPaymentNum().equals(paymentNum);
	}

	@Test
	void findAllTest() {
		Long userNum = 1L;
		String userId = "sem1308";
		// by userNum
		List<Payment> paymentList1 = paymentService.findByUserNum(userNum);

		// by userId
		List<Payment> paymentList2 = paymentService.findByUserId(userId);
	}
}
