package uos.cineseoul.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.dto.create.CreateTicketAudienceDTO;
import uos.cineseoul.dto.create.CreateTicketDTO;
import uos.cineseoul.dto.insert.InsertTicketDTO;
import uos.cineseoul.dto.response.PrintTicketDTO;
import uos.cineseoul.dto.update.UpdateTicketDTO;
import uos.cineseoul.entity.*;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.repository.*;
import uos.cineseoul.service.ScheduleService;
import uos.cineseoul.service.SeatService;
import uos.cineseoul.service.TicketService;
import uos.cineseoul.service.UserService;
import uos.cineseoul.utils.enums.AudienceType;
import uos.cineseoul.utils.enums.TicketState;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class TicketServiceTests {
	@Autowired
	TicketService ticketService;

	@Autowired
	ScheduleSeatService scheduleSeatService;
	@Autowired
	ScheduleSeatRepository scheduleSeatRepository;

	@Autowired
	ScheduleRepository scheduleRepository;

	@Autowired
	MovieRepository movieRepository;

	@Autowired
	ScreenRepository screenRepository;

	@Autowired
	SeatRepository seatRepository;

	@Autowired
	UserRepository userRepository;

	// 픽스처
	Screen screen = Screen.mock();
	Seat seat = Seat.mock(screen);
	Movie movie = Movie.mock();
	Schedule schedule = Schedule.mock(screen,movie);
	ScheduleSeat scheduleSeat;

	User user = User.mock();
	@BeforeEach
	public void init(){
		// 상영관 등록
		screenRepository.save(screen);

		// 상영관에 좌석 등록
		seat = seatRepository.save(seat);
		screen.getSeats().add(seat);

		// 영화 등록
		movieRepository.save(movie);

		// 상영일정 등록
		scheduleRepository.save(schedule);

		// 상영일정_좌석 등록
		scheduleSeatService.insertScheduleSeat(schedule,screen);
		scheduleSeat = scheduleSeatService.findScheduleSeat(schedule.getSchedNum(), seat.getSeatNum());

		userRepository.save(user);
		
		// 좌석 선택
		scheduleSeat.select(user);
	}

	@Test
	@Transactional
	void generateTicketTest() {
		Integer stdPrice = seat.getSeatGrade().getPrice();
		Integer salePrice = stdPrice - AudienceType.G.getDiscountPrice();

		InsertTicketDTO ticketDTO = InsertTicketDTO.builder().stdPrice(stdPrice)
			.ticketState(TicketState.N).schedule(schedule).user(user).build();

		List<Long> seatNumList = new ArrayList<>();
		seatNumList.add(seat.getSeatNum());

		List<CreateTicketAudienceDTO> audienceTypeDTOList = new ArrayList<>();
		audienceTypeDTOList.add(CreateTicketAudienceDTO.builder().audienceType(AudienceType.G).count(1).build());

		Ticket ticket = ticketService.insert(ticketDTO,seatNumList,audienceTypeDTOList);

		Assertions.assertThat(ticket.getTicketSeats().size()).isEqualTo(seatNumList.size());
		Assertions.assertThat(ticket.getUser().getUserNum()).isEqualTo(user.getUserNum());
		Assertions.assertThat(ticket.getSchedule().getSchedNum()).isEqualTo(schedule.getSchedNum());
		Assertions.assertThat(ticket.getTicketState()).isEqualTo(TicketState.N);
		Assertions.assertThat(ticket.getStdPrice()).isEqualTo(stdPrice);
		Assertions.assertThat(ticket.getSalePrice()).isEqualTo(salePrice);
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
	}
}
