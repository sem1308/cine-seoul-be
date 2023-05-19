package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.entity.Schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;

import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.repository.ScheduleRepository;
import uos.cineseoul.repository.ScheduleSeatRepository;
import uos.cineseoul.repository.ScreenRepository;
import uos.cineseoul.repository.SeatRepository;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class ScheduleRepoTests {
	@Autowired
	ScheduleRepository scheduleRepo;
	@Autowired
	ScreenRepository screenRepo;
	@Autowired
	ScheduleSeatRepository scheduleSeatRepo;
	@Test
	//@Transactional
	void registerTest() {
		// 상영 일정 등록
		Integer order = 1;
		String screenName = "A";
		Screen screen = screenRepo.findByName(screenName).get();
		Integer emptySeat = screen.getTotalSeat();

		LocalDateTime schedTime = LocalDateTime.now();
		Schedule schedule = Schedule.builder().order(order).screen(screen).emptySeat(emptySeat).schedTime(schedTime).build();

		Schedule savedSched = scheduleRepo.save(schedule);

		assert savedSched.getOrder().equals(order) && savedSched.getScreen().equals(screen) &&
				savedSched.getEmptySeat().equals(emptySeat) && savedSched.getSchedTime().equals(schedTime);

		List<Seat> seatList = savedSched.getScreen().getSeats();

		// Screen에서 EAGER로 해야함
		seatList.forEach(seat -> {
			ScheduleSeat ss = ScheduleSeat.builder().schedule(savedSched).seat(seat).occupied("N").build();
			scheduleSeatRepo.save(ss);
		});
	}

	@Test
	@Transactional
	void findTest() {
		String screenName = "A";
		Screen screen = screenRepo.findByName(screenName).get();
		Integer emptySeat = screen.getTotalSeat();

		// 상영일정 2개 생성
		List<Schedule> schedList = new ArrayList<Schedule>();
		
		LocalDateTime schedTime1 = LocalDateTime.now();
		Schedule schedule1 = Schedule.builder().order(1).screen(screen).emptySeat(emptySeat).schedTime(schedTime1).build();

		LocalDateTime schedTime2 = LocalDateTime.of(2023,05,18,10,30,30);
		Schedule schedule2 = Schedule.builder().order(2).screen(screen).emptySeat(emptySeat).schedTime(schedTime2).build();

		schedList.add(schedule1);
		schedList.add(schedule2);
		schedList.forEach(s -> {
			scheduleRepo.save(s);
			System.out.println("입력하려는 상영일자 : " + s.getSchedTime());
		});

		// 오늘의 상영일정 검색
		LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now().minusDays(0), LocalTime.of(0,0,0));
		LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));

		System.out.println("오늘 시작 날짜 : " + startDatetime);
		System.out.println("오늘 끝 날짜 : " + endDatetime);

		List<Schedule> scheduleList = scheduleRepo.findAllBySchedTimeBetween(startDatetime, endDatetime);

		scheduleList.forEach(sched -> {
			System.out.println("오늘 상영일자 : " + sched.getSchedTime());
		});
	}
}
