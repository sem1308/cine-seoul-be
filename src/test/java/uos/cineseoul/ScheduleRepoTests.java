package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.entity.Schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.repository.ScheduleRepository;
import uos.cineseoul.repository.ScreenRepository;
import uos.cineseoul.repository.SeatRepository;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@Slf4j
class ScheduleRepoTests {
	@Autowired
	SeatRepository seatRepo;
	@Autowired
	ScheduleRepository scheduleRepo;
	@Autowired
	ScreenRepository screenRepo;
	@Test
	void registerTest() {
		// 상영 일정 등록
		Integer order = 1;
		String screenName = "A";
		Screen screen = setScreenAndSeat(screenName);
		Integer emptySeat = screen.getTotalSeat();

		LocalDateTime schedTime = LocalDateTime.now();
		Schedule schedule = Schedule.builder().order(order).screen(screen).emptySeat(emptySeat).schedTime(schedTime).build();

		Schedule savedSched = scheduleRepo.save(schedule);

		assert savedSched.getOrder().equals(order) && savedSched.getScreen().equals(screen) &&
				savedSched.getEmptySeat().equals(emptySeat) && savedSched.getSchedTime().equals(schedTime);
	}

	@Test
	void findTest() {
		String screenName = "A";
		Screen screen = setScreenAndSeat(screenName);
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

	Screen setScreenAndSeat(String screenName){
		Screen screen = Screen.builder().name(screenName).totalSeat(0).build();

		Screen savedScreen = screenRepo.save(screen);

		// 상영관에 좌석 2개 생성
		List<Seat> seatList = new ArrayList<Seat>();

		String row1 = "H";
		String col1 = "10";
		String seatGrade1 = "A";
		Seat seat1 = Seat.builder().row(row1).col(col1).seatGrade(seatGrade1).screen(savedScreen).build();
		seatList.add(seat1);

		String row2 = "E";
		String col2 = "15";
		String seatGrade2 = "B";
		Seat seat2 = Seat.builder().row(row2).col(col2).seatGrade(seatGrade2).screen(savedScreen).build();
		seatList.add(seat2);

		seatList.forEach(seat -> {
			Seat saved = seatRepo.save(seat);
			saved.getScreen().setTotalSeat(saved.getScreen().getTotalSeat() + 1);
			screenRepo.save(saved.getScreen());
		});

		return screenRepo.findByName(savedScreen.getName()).orElseThrow(()->{
			throw new RuntimeException("screen " + savedScreen.getName() + " is not exits");
		});
	}
}
