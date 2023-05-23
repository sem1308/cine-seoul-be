package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import uos.cineseoul.dto.InsertTicketDTO;
import uos.cineseoul.dto.UpdateTicketDTO;
import uos.cineseoul.entity.*;
import uos.cineseoul.mapper.TicketMapper;
import uos.cineseoul.repository.*;

import javax.transaction.Transactional;
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
	@Rollback(false)
	void generateTicketTest() {
		Integer stdPrice = 8000;
		Integer salePrice = 7500;
		String issued = "N";

		Long userNum = 1L;
		Long schedNum = 2L;
		Long seatNum = 1L;

		InsertTicketDTO ticketDTO = InsertTicketDTO.builder().stdPrice(stdPrice).salePrice(salePrice)
				.issued(issued).userNum(userNum).schedNum(schedNum).seatNum(seatNum).build();

		User user = userRepo.findById(userNum).get();
		ScheduleSeat scheduleSeat = scheduleSeatRepo.findBySchedNumAndSeatNum(ticketDTO.getSchedNum(),ticketDTO.getSeatNum()).get();
		scheduleSeat.setOccupied("Y");
		scheduleSeatRepo.save(scheduleSeat);

		Ticket ticket = TicketMapper.INSTANCE.toEntity(ticketDTO);
		ticket.setUser(user);
		ticket.setScheduleSeat(scheduleSeat);

		Ticket savedTicket = ticketRepo.save(ticket);

		assert ticket.getCreatedAt().equals(savedTicket.getCreatedAt());
	}

	@Test
	@Transactional
	void updateTicketTest() {
		Integer salePrice = 8000; // 바꿈
		String issued = "N";
		Long ticketNum = 1L;
		Long schedNum = 2L;
		Long seatNum = 2L; // 바꿈

		UpdateTicketDTO ticketDTO = UpdateTicketDTO.builder().salePrice(salePrice)
				.issued(issued).ticketNum(ticketNum).schedNum(schedNum).seatNum(seatNum).build();

		Ticket ticket = ticketRepo.findById(ticketNum).get();

		ScheduleSeat scheduleSeat = scheduleSeatRepo.findBySchedNumAndSeatNum(ticketDTO.getSchedNum(),ticketDTO.getSeatNum()).get();

		TicketMapper.INSTANCE.updateFromDto(ticketDTO,ticket);
		ticket.setScheduleSeat(scheduleSeat);

		Ticket savedTicket = ticketRepo.save(ticket);

		assert savedTicket.getSalePrice().equals(salePrice);
		assert savedTicket.getScheduleSeat().getSeat().getSeatNum().equals(seatNum);
	}

	@Test
	@Transactional
	void findOneTest() {
		Long ticketNum = 2L;

		// by userId and ticketNum
		Ticket ticket = ticketRepo.findById(ticketNum).orElseThrow(() -> {
			throw new RuntimeException("Ticket is not exits");
		});

		assert ticket.getTicketNum().equals(ticketNum);

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
