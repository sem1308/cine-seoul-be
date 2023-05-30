package uos.cineseoul.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.dto.insert.InsertScheduleDTO;
import uos.cineseoul.dto.insert.InsertScreenDTO;
import uos.cineseoul.dto.update.UpdateScheduleDTO;
import uos.cineseoul.entity.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.mapper.ScheduleMapper;
import uos.cineseoul.mapper.ScreenMapper;
import uos.cineseoul.repository.ScheduleRepository;
import uos.cineseoul.repository.ScheduleSeatRepository;
import uos.cineseoul.repository.ScreenRepository;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
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
	@Transactional
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

		// @Transactional 또는 Screen에서 EAGER로 해야함
		seatList.forEach(seat -> {
			ScheduleSeat ss = ScheduleSeat.builder().schedule(savedSched).seat(seat).build();
			scheduleSeatRepo.save(ss);
		});
	}

	@Test
	@Transactional
	void registerTestByMapper() throws ParseException {
		// 상영 일정 등록
		Integer order = 1;
		String myString = "2023-05-21 10:30:30";
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = dtFormat.parse(myString);
		LocalDateTime schedTime = new java.sql.Timestamp(date.getTime()).toLocalDateTime();
		//LocalDateTime schedTime = LocalDateTime.of(2023,05,21,10,30,30);

		Long screenNum = 1L;

		Screen screen = screenRepo.findById(screenNum).get();
		InsertScheduleDTO scheduleDTO = InsertScheduleDTO.builder().order(order)
										.screen(screen).schedTime(schedTime).build();

		Schedule schedule = ScheduleMapper.INSTANCE.toEntity(scheduleDTO);

		Integer emptySeat = screen.getTotalSeat();
		schedule.setEmptySeat(emptySeat);

		Schedule savedSched = scheduleRepo.save(schedule);

		assert savedSched.getOrder().equals(order) && savedSched.getScreen().equals(screen) &&
				savedSched.getEmptySeat().equals(emptySeat) && savedSched.getSchedTime().equals(schedTime);

		List<Seat> seatList = savedSched.getScreen().getSeats();

		// Screen에서 EAGER로 해야함
		seatList.forEach(seat -> {
			ScheduleSeat ss = ScheduleSeat.builder().schedule(savedSched).seat(seat).build();
			scheduleSeatRepo.save(ss);
		});
	}

	@Test
	@Transactional
	void updateTestByMapper() throws ParseException {
		// 상영 일정 변경
		//Integer order = 1;
//		String myString = "2023-05-21 12:30:30"; // 원래는 2023-05-21 10:30:30
//		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date = dtFormat.parse(myString);
//		LocalDateTime schedTime = new java.sql.Timestamp(date.getTime()).toLocalDateTime();
//
//		UpdateScheduleDTO scheduleDTO = UpdateScheduleDTO.builder().schedNum(2L).schedTime(schedTime).build();

		// 상영관 변경
		String name = "B";
		InsertScreenDTO screenDTO = InsertScreenDTO.builder().name(name).build();
		Screen s = ScreenMapper.INSTANCE.toEntity(screenDTO);
		Screen saved = screenRepo.save(s);

		UpdateScheduleDTO scheduleDTO = UpdateScheduleDTO.builder().screen(saved).build();

		Schedule schedule = scheduleRepo.findById(2L).get();

		ScheduleMapper.INSTANCE.updateFromDto(scheduleDTO, schedule);

		Screen screen = null;
		Integer emptySeat = 0;
		if(scheduleDTO.getScreen()!=null){
			screen = screenRepo.findById(scheduleDTO.getScreen().getScreenNum()).orElse(null);
			emptySeat = screen.getTotalSeat();
			schedule.setScreen(screen);
			schedule.setEmptySeat(emptySeat);
		}

		Schedule savedSched = scheduleRepo.save(schedule);

		//assert savedSched.getSchedTime().equals(schedTime);
		//System.out.println("변경된 상영일정 : " + savedSched.getSchedTime());

		//assert savedSched.getOrder().equals(order);
		if(scheduleDTO.getScreen().getScreenNum()!=null){
			assert savedSched.getEmptySeat().equals(emptySeat);
			assert savedSched.getScreen().equals(screen);

			List<Seat> seatList = screen.getSeats();

			savedSched.getScheduleSeats().forEach(ss -> {
				scheduleSeatRepo.delete(ss);
			});

			if(seatList != null){
				// Screen에서 EAGER로 해야함
				seatList.forEach(seat -> {
					ScheduleSeat ss = ScheduleSeat.builder().schedule(savedSched).seat(seat).build();
					scheduleSeatRepo.save(ss);
				});
			}
		}
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
