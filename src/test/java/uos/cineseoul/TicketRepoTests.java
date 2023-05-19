package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.entity.*;
import uos.cineseoul.repository.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Slf4j
class TicketRepoTests {
	@Autowired
	TicketRepository ticketRepo;
	@Autowired
	UserRepository userRepo;
	@Autowired
	ScheduleSeatRepository scheduleSeatRepo;
	@Test
	@Transactional
	void generateTicketTest() {
		Integer stdPrice = 8000;
		Integer salePrice = 7500;
		String issued = "N";
		LocalDateTime createdAt = LocalDateTime.now();

		String userID = "sem1308";

		User user = userRepo.findByUserId(userID).get();

		Long schedNum = 5L;
		Long seatNum = 1L;
		ScheduleSeat scheduleSeat = scheduleSeatRepo.findBySchedNumAndSeatNum(schedNum,seatNum).get();

		Ticket ticket = Ticket.builder().stdPrice(stdPrice).salePrice(salePrice)
				.issued(issued).createdAt(createdAt).user(user).scheduleSeat(scheduleSeat).build();

		Ticket savedTicket = ticketRepo.save(ticket);

		assert ticket.getCreatedAt().equals(savedTicket.getCreatedAt());
	}
	@Test
	@Transactional
	void findOneTest() {
		Long ticketNum = 2L;
		Long userNum = 1L;
		String userId = "sem1308";

		// by userNum and ticketNum
		Ticket ticket1 = ticketRepo.findByUserNumAndTicketNum(userNum, ticketNum).orElseThrow(() -> {
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
		Long userNum = 1L;
		String userId = "sem1308";
		// by userNum
		List<Ticket> ticketList1 = ticketRepo.findByUserNum(userNum);

		// by userId
		List<Ticket> ticketList2 = ticketRepo.findByUserID(userId);
	}
}
