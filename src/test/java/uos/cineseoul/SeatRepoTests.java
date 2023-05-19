package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.repository.ScreenRepository;
import uos.cineseoul.repository.SeatRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class SeatRepoTests {
	@Autowired
	SeatRepository seatRepo;
	@Autowired
	ScreenRepository screenRepo;
	@Test
	//@Transactional
	void registerTest() {
		// 상영관 불러오기
		String screenName = "A";
		Screen screen = screenRepo.findByName(screenName).get();

		// 상영관에 좌석 2개 생성
		List<Seat> seatList = new ArrayList<Seat>();

		String row1 = "H";
		String col1 = "10";
		String seatGrade1 = "A";
		Seat seat1 = Seat.builder().row(row1).col(col1).seatGrade(seatGrade1).screen(screen).build();
		seatList.add(seat1);

		String row2 = "E";
		String col2 = "15";
		String seatGrade2 = "B";
		Seat seat2 = Seat.builder().row(row2).col(col2).seatGrade(seatGrade2).screen(screen).build();
		seatList.add(seat2);

		seatList.forEach(seat -> {
			Seat saved = seatRepo.save(seat);
			// 좌석 행, 렬, 등급 저장 확인
			assert saved.getRow().equals(seat.getRow()) && saved.getCol().equals(seat.getCol())
														&& saved.getSeatGrade().equals(seat.getSeatGrade());

			// 같은 상영관인지 테스트
			assert saved.getScreen().equals(seat.getScreen());

			saved.getScreen().setTotalSeat(saved.getScreen().getTotalSeat() + 1);
			screenRepo.save(saved.getScreen());
		});

		Screen s = screenRepo.findByName(screenName).get();
		// 상영관 좌석 수 확인
		assert s.getTotalSeat().equals(seatList.size());
	}
}
