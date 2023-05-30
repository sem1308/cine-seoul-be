package uos.cineseoul.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.dto.insert.InsertTicketDTO;
import uos.cineseoul.dto.response.PrintTicketDTO;
import uos.cineseoul.dto.update.UpdateTicketDTO;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.service.ScheduleService;
import uos.cineseoul.service.SeatService;
import uos.cineseoul.service.TicketService;
import uos.cineseoul.service.UserService;
import uos.cineseoul.utils.enums.TicketState;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Slf4j
class TicketServiceTests {
	@Autowired
	TicketService ticketService;
	@Autowired
	UserService userService;
	@Autowired
	ScheduleService scheduleService;
	@Test
	@Transactional
	//@Rollback(false)
	void generateTicketTest() {
		Integer stdPrice = 8000;
		Integer salePrice = 7500;
		String issued = "N";

		Long userNum = 1L;
		Long schedNum = 2L;
		Long seatNum = 1L;

		InsertTicketDTO ticketDTO = InsertTicketDTO.builder().stdPrice(stdPrice).salePrice(salePrice)
				.user(userService.findOneByNum(userNum)).scheduleSeat(scheduleService.findScheduleSeat(schedNum,seatNum)).build();

		Ticket savedTicket = ticketService.insert(ticketDTO);

		assert savedTicket.getSalePrice().equals(salePrice);
		assert savedTicket.getScheduleSeat().getSeat().equals(seatNum);
	}

	@Test
	@Transactional
	void updateTicketTest() {
		Integer salePrice = 8000; // 바꿈
		Long ticketNum = 21L;
		Long seatNum = 2L; // 바꿈

		UpdateTicketDTO ticketDTO = UpdateTicketDTO.builder().salePrice(salePrice)
				.ticketState(TicketState.N).build();

		Ticket savedTicket = ticketService.update(ticketNum,ticketDTO);

		assert savedTicket.getSalePrice().equals(salePrice);
		assert savedTicket.getScheduleSeat().getSeat().getSeatNum().equals(seatNum);
	}

	@Test
	@Transactional
	void findOneTest() {
		Long ticketNum = 21L;

		// by userId and ticketNum
		Ticket ticket = ticketService.findOneByNum(ticketNum);
		assert ticket.getTicketNum().equals(ticketNum);

		System.out.println("티켓 1개 찾기 테스트 완료");
	}

	@Test
	void findAllTest() {
		Long userNum = 1L;
		String userId = "sem1308";
		// by userNum
		List<Ticket> ticketList1 = ticketService.findByUserNum(userNum);

		System.out.println("유저 "+userId+"의 티켓 수: "+ticketList1.size());
		// by userId
		List<Ticket> ticketList2 = ticketService.findByUserId(userId);
		System.out.println(userNum+"번 유저의 티켓 수: "+ticketList2.size());
	}
}
