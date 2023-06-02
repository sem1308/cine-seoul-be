package uos.cineseoul.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import uos.cineseoul.dto.insert.InsertTicketDTO;
import uos.cineseoul.dto.update.UpdateTicketDTO;
import uos.cineseoul.entity.*;
import uos.cineseoul.mapper.TicketMapper;
import uos.cineseoul.repository.*;
import uos.cineseoul.utils.enums.Is;
import uos.cineseoul.utils.enums.TicketState;

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

		Long userNum = 1L;
		Long schedNum = 2L;
		Long seatNum = 1L;

		User user = userRepo.findById(userNum).get();
		ScheduleSeat scheduleSeat = scheduleSeatRepo.findBySchedNumAndSeatNum(schedNum,seatNum).get();

		InsertTicketDTO ticketDTO = InsertTicketDTO.builder().stdPrice(stdPrice).salePrice(salePrice)
				.user(user).scheduleSeat(scheduleSeat).build();
		scheduleSeat.setIsOccupied(Is.N);
		scheduleSeatRepo.save(scheduleSeat);

		Ticket ticket = TicketMapper.INSTANCE.toEntity(ticketDTO);

		Ticket savedTicket = ticketRepo.save(ticket);

		assert ticket.getCreatedAt().equals(savedTicket.getCreatedAt());
	}

	@Test
	@Transactional
	@Rollback(false)
	void updateTicketTest() {
		Integer salePrice = 8200;
		Long ticketNum = 4L;

		UpdateTicketDTO ticketDTO = UpdateTicketDTO.builder().salePrice(salePrice)
				.ticketState(TicketState.N).build();

		Ticket ticket = ticketRepo.findById(ticketNum).get();

		TicketMapper.INSTANCE.updateFromDto(ticketDTO,ticket);

		Ticket savedTicket = ticketRepo.save(ticket);

		assert savedTicket.getSalePrice().equals(salePrice);
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
