package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.dto.InsertSeatDTO;
import uos.cineseoul.dto.UpdateSeatDTO;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.mapper.SeatMapper;
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
	@Transactional
	void registerTest() {

		// 상영관에 좌석 2개 생성
		List<Seat> seatList = new ArrayList<Seat>();

		Long screenNum = 1L;
		String row1 = "H";
		String col1 = "10";
		String seatGrade1 = "A";

		InsertSeatDTO seatDTO1 = InsertSeatDTO.builder().row(row1).col(col1)
				.seatGrade(seatGrade1).screenNum(screenNum).build();

		// 상영관 불러오기
		Screen screen = screenRepo.findById(seatDTO1.getScreenNum()).get();

		Integer totalSeat = screen.getTotalSeat();

		Seat seat1 = SeatMapper.INSTANCE.toEntity(seatDTO1);
		seat1.setScreen(screen);
		seatList.add(seat1);

		String row2 = "E";
		String col2 = "15";
		String seatGrade2 = "B";

		InsertSeatDTO seatDTO2 = InsertSeatDTO.builder().row(row2).col(col2)
				.seatGrade(seatGrade2).screenNum(screenNum).build();

		Seat seat2 = SeatMapper.INSTANCE.toEntity(seatDTO2);
		seat2.setScreen(screen);
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

		Screen s = screenRepo.findById(screenNum).get();
		// 상영관 좌석 수 확인
		assert s.getTotalSeat().equals(totalSeat+seatList.size());
	}
	@Test
	@Transactional
	void updateTest() {
		Long seatNum = 1L;
		//Long screenNum = 1L;
		String row = "D"; // 원래 H
		String col = "15"; // 원래 10
		String seatGrade = "C"; // 원래 A

		Seat seat = seatRepo.findById(seatNum).get();

		UpdateSeatDTO seatDTO = UpdateSeatDTO.builder().row(row).col(col)
				.seatGrade(seatGrade).build();

		SeatMapper.INSTANCE.updateFromDto(seatDTO,seat);

		Seat updatedSeat = seatRepo.save(seat);

		assert updatedSeat.getRow().equals(row);
		assert updatedSeat.getCol().equals(col);
		assert updatedSeat.getSeatGrade().equals(seatGrade);
	}
}
