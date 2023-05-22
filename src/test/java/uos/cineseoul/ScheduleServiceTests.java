package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import uos.cineseoul.dto.InsertScheduleDTO;
import uos.cineseoul.dto.UpdateScheduleDTO;
import uos.cineseoul.entity.Schedule;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.service.ScheduleService;
import uos.cineseoul.service.ScreenService;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Slf4j
class ScheduleServiceTests {
	@Autowired
	ScheduleService scheduleService;
	@Autowired
	ScreenService screenService;
	@Test
	@Transactional
	@Rollback(false)
	void registerTest() throws ParseException {
		// 상영 일정 등록
		Integer order = 1;
		String myString = "2023-05-22 10:30:30";
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = dtFormat.parse(myString);
		LocalDateTime schedTime = new java.sql.Timestamp(date.getTime()).toLocalDateTime();
		//LocalDateTime schedTime = LocalDateTime.of(2023,05,21,10,30,30);
		Long screenNum = 61L;

		InsertScheduleDTO scheduleDTO = InsertScheduleDTO.builder().order(order)
										.screenNum(screenNum).schedTime(schedTime).build();

		Schedule schedule = scheduleService.insert(scheduleDTO);
	}

	@Test
	@Transactional()
	@Rollback(false)
	void updateTestByMapper() throws ParseException {
		// 상영 일정 변경
		//Integer order = 1;
		String myString = "2023-05-21 12:30:30"; // 원래는 2023-05-21 10:30:30
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = dtFormat.parse(myString);
		LocalDateTime schedTime = new java.sql.Timestamp(date.getTime()).toLocalDateTime();

		// 상영관 변경
		String name = "B";
		Long schedNum = 44L;
		Screen screen = screenService.findOneByName(name);
		UpdateScheduleDTO scheduleDTO = UpdateScheduleDTO.builder().schedNum(schedNum).screenNum(screen.getScreenNum()).schedTime(schedTime).build();

		Schedule schedule = scheduleService.update(scheduleDTO);

//		System.out.println("변경된 상영관 이름 : " + schedule.getScreen().getName());
//		System.out.println("변경된 상영관 총 좌석 개수 : " + schedule.getScreen().getTotalSeat());
//		System.out.println("변경된 상영일정-좌석 총 개수 : " + schedule.getScheduleSeats().size());
//		schedule.getScheduleSeats().forEach(ss -> {
//			System.out.println("변경된 상영일정 번호 : " + ss.getSchedule().getSchedNum() + ", 좌석 번호 : " + ss.getSeat().getSeatNum());
//		});
	}

	@Test
	@Transactional
	void findTest() {
		LocalDateTime schedTime = LocalDateTime.now();

		List<Schedule> scheduleList = scheduleService.findByDate(schedTime);
		scheduleList.forEach(s -> {
			System.out.println("상영시간 : " + s.getSchedTime());
		});
	}
}
