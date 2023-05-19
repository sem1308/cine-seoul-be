package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.repository.TicketRepository;
import uos.cineseoul.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
@Slf4j
class TicketRepoTests {
	@Autowired
	TicketRepository ticketRepo;
	UserRepository userRepo;
	@Test
	void addTicket() {
	}
	@Test
	void findOneTest() {
		Long ticketNum = 1L;
		Long userNum = 1L;
		String userId = "sem1308";

		// by userNum and ticketNum
		Ticket ticket1 = ticketRepo.findByUserAndTicketNum(userNum, ticketNum).orElseThrow(() -> {
			throw new RuntimeException("Ticket1 is not exits");
		});

		// by userId and ticketNum
		Ticket ticket2 = ticketRepo.findByUserIDAndTicketNum(userId, ticketNum).orElseThrow(() -> {
			throw new RuntimeException("Ticket2 is not exits");
		});

		assert ticket1.getTicketNum().equals(ticket2.getTicketNum());

		System.out.println("티켓 1개 찾기 테스트 완료");
	}

	@Test
	void findAllTest() {
		// by userNum

		// by userId
	}

}
