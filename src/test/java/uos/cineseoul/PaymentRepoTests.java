package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.dto.InsertPaymentDTO;
import uos.cineseoul.entity.*;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.PaymentMapper;
import uos.cineseoul.repository.*;
import uos.cineseoul.utils.enums.PayState;
import uos.cineseoul.utils.enums.PaymentMethodType;

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

		PaymentMethodType paymentMethodCode = PaymentMethodType.C000;

		PaymentMethod pm = paymentMethodRepo.findById(paymentMethodCode).orElse(null);

		Payment payment = Payment.builder().ticket(ticket).approvalNum("0XASDWU123")
				.createdAt(createdAt).price(price).user(user).paymentMethod(pm).build();

		Payment savedPayment = paymentRepo.save(payment);
	}

	@Test
	@Transactional
	void paymentTestByMapper() {
		Integer price = 7500;

		Long userNum = 1L;
		Long ticketNum = 1L;
		PaymentMethodType paymentMethodCode = PaymentMethodType.C000;

		InsertPaymentDTO paymentDTO = InsertPaymentDTO.builder().ticketNum(ticketNum)
				.price(price).userNum(userNum).paymentMethodCode(paymentMethodCode).build();

		Payment payment = PaymentMapper.INSTANCE.toEntity(paymentDTO);

		if(paymentDTO.getPaymentMethodCode().equals(PaymentMethodType.C000)){
			payment.setApprovalNum("0XASDWU123");
		}

		User user = userRepo.findById(userNum).get();
		Ticket ticket = ticketRepo.findById(ticketNum).get();
		PaymentMethod pm = paymentMethodRepo.findById(paymentMethodCode).get();

		payment.setUser(user);
		payment.setTicket(ticket);
		payment.setPaymentMethod(pm);

		Payment savedPayment = paymentRepo.save(payment);

		ticket.setIssued("Y");
		ticketRepo.save(ticket);
	}

	@Test
	//@Transactional
	void generatePaymentMethodTest(){
		PaymentMethod pm1 = PaymentMethod.builder().paymentMethodCode(PaymentMethodType.C000).name("card").build();
		PaymentMethod pm2 = PaymentMethod.builder().paymentMethodCode(PaymentMethodType.A000).name("account").build();

		paymentMethodRepo.save(pm1);
		paymentMethodRepo.save(pm2);
	}

	@Test
	void findOneTest() {
		Long ticketNum = 2L;
		Payment payment = paymentRepo.findByTicketNum(ticketNum).get();

		assert payment.getTicket().getTicketNum().equals(ticketNum);
	}

	@Test
	void findOneByNumAndStateTest() {
		Long ticketNum = 4L;

		Payment payment = paymentRepo.findByTicketNumAndState(ticketNum, PayState.Y).orElseThrow(()->{
			throw new ResourceNotFoundException("결제된 티켓이 아닙니다.");
		});

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
