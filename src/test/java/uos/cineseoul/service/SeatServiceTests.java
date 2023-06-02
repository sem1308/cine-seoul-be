package uos.cineseoul.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import uos.cineseoul.dto.insert.InsertSeatDTO;
import uos.cineseoul.dto.update.UpdateSeatDTO;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.repository.ScreenRepository;
import uos.cineseoul.utils.enums.SeatGrade;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Slf4j
class SeatServiceTests {
	@Autowired
	SeatService seatService;
	@Autowired
	ScreenRepository screenRepo;
	@Test
	@Transactional
	@Rollback(false)
	void registerTest() {
		Long screenNum = 1L;
		String row1 = "E";
		String col1 = "11";
		SeatGrade seatGrade1 = SeatGrade.A;

		Screen screenBefore = screenRepo.findById(screenNum).get();
		Integer screenBeforeTS = screenBefore.getTotalSeat();

		InsertSeatDTO seatDTO = InsertSeatDTO.builder().row(row1).col(col1)
				.seatGrade(seatGrade1).screen(screenBefore).build();

		Seat savedSeat = seatService.insert(seatDTO);
		Screen screenAfter = screenRepo.findById(screenNum).get();

		assert screenAfter.getTotalSeat().equals(screenBeforeTS+1);
	}

	@Test
	@Transactional
	void updateTest() {
		Long seatNum = 1L;
		Long screenNum = 1L;
		String row = "D"; // 원래 H
		String col = "15"; // 원래 10
		SeatGrade seatGrade = SeatGrade.C; // 원래 A

		Seat basicSeat = seatService.findOneByNum(seatNum);
		Screen basicScreen = basicSeat.getScreen();

		Screen updatedScreen = screenRepo.findById(screenNum).get();
		UpdateSeatDTO seatDTO = UpdateSeatDTO.builder().row(row).screen(updatedScreen)
								.col(col).seatGrade(seatGrade).build();

		Long basicScreenNum = basicScreen.getScreenNum();
		Integer basicScreenTotalSeat = screenRepo.findById(basicScreenNum).get().getTotalSeat();
		Long updatedScreenNum = updatedScreen.getScreenNum();
		Integer updatedScreenTotalSeat = screenRepo.findById(updatedScreenNum).get().getTotalSeat();

		Seat updatedSeat = seatService.update(seatNum,seatDTO);

		Integer cBasicScreenTotalSeat = screenRepo.findById(basicScreenNum).get().getTotalSeat();
		Integer cUpdatedScreenTotalSeat = screenRepo.findById(updatedScreenNum).get().getTotalSeat();

		System.out.println("기존 From 스크린 "+basicScreenNum+" 좌석 합 : "+basicScreenTotalSeat);
		System.out.println("기존 To 스크린 "+updatedScreenNum+" 좌석 합 : "+updatedScreenTotalSeat);
		System.out.println("변경 From 스크린 "+basicScreenNum+" 좌석 합 : "+cBasicScreenTotalSeat);
		System.out.println("변경 To 스크린 "+updatedScreenNum+" 좌석 합 : "+cUpdatedScreenTotalSeat);

		assert cBasicScreenTotalSeat.equals(basicScreenNum-1);
		assert cUpdatedScreenTotalSeat.equals(updatedScreenTotalSeat+1);
	}

	@Test
	@Transactional
	void findAllByScreenTest() {
		Long screenNum = 1L;

		List<Seat> seat = seatService.findAllByScreenNum(screenNum);
		System.out.println(seat.size());
	}

}
