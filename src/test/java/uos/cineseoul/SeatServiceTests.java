package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.dto.InsertSeatDTO;
import uos.cineseoul.dto.UpdateSeatDTO;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.repository.ScreenRepository;
import uos.cineseoul.service.SeatService;

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
	//@Transactional
	void registerTest() {
		Long screenNum = 61L;
		String row1 = "E";
		String col1 = "15";
		String seatGrade1 = "A";

		InsertSeatDTO seatDTO = InsertSeatDTO.builder().row(row1).col(col1)
				.seatGrade(seatGrade1).screenNum(screenNum).build();
		Integer screenBeforeTS = screenRepo.findById(seatDTO.getScreenNum()).get().getTotalSeat();

		Seat savedSeat = seatService.insert(seatDTO);
		Screen screenAfter = screenRepo.findById(seatDTO.getScreenNum()).get();

		assert screenAfter.getTotalSeat().equals(screenBeforeTS+1);
	}

	@Test
	@Transactional
	void updateTest() {
		Long seatNum = 1L;
		Long screenNum = 1L;
		String row = "D"; // 원래 H
		String col = "15"; // 원래 10
		String seatGrade = "C"; // 원래 A

		Seat basicSeat = seatService.findOneByNum(seatNum);
		Screen basicScreen = basicSeat.getScreen();

		Screen updatedScreen = screenRepo.findById(screenNum).get();
		UpdateSeatDTO seatDTO = UpdateSeatDTO.builder().seatNum(seatNum).row(row).screenNum(screenNum)
								.col(col).seatGrade(seatGrade).build();

		Long basicScreenNum = basicSeat.getScreen().getScreenNum();
		Integer basicScreenTotalSeat = basicSeat.getScreen().getTotalSeat();
		Long updatedScreenNum = updatedScreen.getScreenNum();
		Integer updatedScreenTotalSeat = updatedScreen.getTotalSeat();

		Seat updatedSeat = seatService.update(seatDTO);

		System.out.println("기존 From 스크린 "+basicScreenNum+" 좌석 합 : "+basicScreenTotalSeat);
		System.out.println("기존 To 스크린 "+updatedScreenNum+" 좌석 합 : "+updatedScreenTotalSeat);
		System.out.println("변경 From 스크린 "+basicScreen.getScreenNum()+" 좌석 합 : "+basicScreen.getTotalSeat());
		System.out.println("변경 To 스크린 "+updatedSeat.getScreen().getScreenNum()+" 좌석 합 : "+updatedSeat.getScreen().getTotalSeat());

		if(seatDTO.getScreenNum()==null || seatDTO.getScreenNum().equals(basicScreenNum))
			assert basicScreenTotalSeat.equals(updatedSeat.getScreen().getTotalSeat());
		else{
			assert basicScreen.getTotalSeat().equals(basicScreenTotalSeat-1);
			assert updatedSeat.getScreen().getTotalSeat().equals(updatedScreenTotalSeat+1);
		}
	}

	@Test
	@Transactional
	void findAllByScreenTest() {
		Long screenNum = 1L;

		List<Seat> seat = seatService.findAllByScreenNum(screenNum);
		System.out.println(seat.size());
	}

}
