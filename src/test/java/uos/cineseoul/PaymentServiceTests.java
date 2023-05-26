package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.dto.InsertPaymentDTO;
import uos.cineseoul.dto.PrintPaymentDTO;
import uos.cineseoul.entity.PaymentMethod;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.PaymentMethodRepository;
import uos.cineseoul.service.PaymentService;
import uos.cineseoul.utils.enums.PaymentMethodType;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Slf4j
class PaymentServiceTests {
	@Autowired
	PaymentService paymentService;
	@Autowired
	PaymentMethodRepository paymentMethodRepo;
	@Test
	@Transactional
	void paymentTestByMapper() {
		Integer price = 7500;

		Long userNum = 1L;
		Long ticketNum = 1L;
		PaymentMethodType paymentMethodCode = PaymentMethodType.C000;

		InsertPaymentDTO paymentDTO = InsertPaymentDTO.builder().ticketNum(ticketNum)
				.price(price).userNum(userNum).paymentMethodCode(paymentMethodCode).build();

		PrintPaymentDTO savedPayment = paymentService.insert(paymentDTO);
	}

	@Test
	void findOneTest() {
		Long paymentNum = 1L;
		PrintPaymentDTO payment = paymentService.findOneByNum(paymentNum);

		assert payment.getPaymentNum().equals(paymentNum);
	}

	@Test
	void findMethodTest() {
		PaymentMethodType paymentMethodCode = PaymentMethodType.C000;

		PaymentMethod pm = paymentMethodRepo.findById(paymentMethodCode).orElseThrow(()->{
			throw new ResourceNotFoundException(paymentMethodCode +"인 결제 방법이 없습니다.");
		});
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
