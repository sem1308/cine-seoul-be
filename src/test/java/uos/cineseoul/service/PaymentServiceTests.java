package uos.cineseoul.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.dto.insert.InsertPaymentDTO;
import uos.cineseoul.dto.response.PrintPaymentDTO;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.entity.User;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.TicketRepository;
import uos.cineseoul.utils.enums.PaymentMethod;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Slf4j
class PaymentServiceTests {
	@Autowired
	PaymentService paymentService;
	@Autowired
	TicketService ticketService;
	@Autowired
	UserService userService;
	@Test
	@Transactional
	void paymentTestByMapper() {
		Integer price = 7500;

		Long userNum = 1L;
		Long ticketNum = 1L;
		PaymentMethod paymentMethod = PaymentMethod.C00;

		Ticket ticket = ticketService.findOneByNum(ticketNum);
		User user = userService.findOneByNum(ticketNum);

		InsertPaymentDTO paymentDTO = InsertPaymentDTO.builder().ticket(ticket)
				.price(price).user(user).paymentMethod(paymentMethod).build();

		PrintPaymentDTO savedPayment = paymentService.insert(paymentDTO);
	}

	@Test
	void findOneTest() {
		Long paymentNum = 1L;
		PrintPaymentDTO payment = paymentService.findOneByNum(paymentNum);

		assert payment.getPaymentNum().equals(paymentNum);
	}

	@Test
	void findAllTest() {
		Long userNum = 1L;
		String userId = "sem1308";
		// by userNum
		List<PrintPaymentDTO> paymentList1 = paymentService.findByUserNum(userNum);

		// by userId
		List<PrintPaymentDTO> paymentList2 = paymentService.findByUserId(userId);
	}
}
