package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import uos.cineseoul.dto.insert.InsertSeatDTO;
import uos.cineseoul.dto.response.PrintSeatDTO;
import uos.cineseoul.dto.update.UpdateSeatDTO;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.repository.ScreenRepository;
import uos.cineseoul.service.SeatService;
import uos.cineseoul.utils.enums.GradeType;

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
		GradeType seatGrade1 = GradeType.A;

		InsertSeatDTO seatDTO = InsertSeatDTO.builder().row(row1).col(col1)
				.seatGrade(seatGrade1).screenNum(screenNum).build();
		Integer screenBeforeTS = screenRepo.findById(seatDTO.getScreenNum()).get().getTotalSeat();

		PrintSeatDTO savedSeat = seatService.insert(seatDTO);
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
		GradeType seatGrade = GradeType.C; // 원래 A

		PrintSeatDTO basicSeat = seatService.findOneByNum(seatNum);
		Screen basicScreen = screenRepo.findById(basicSeat.getScreenNum()).get();

		Screen updatedScreen = screenRepo.findById(screenNum).get();
		UpdateSeatDTO seatDTO = UpdateSeatDTO.builder().seatNum(seatNum).row(row).screenNum(screenNum)
								.col(col).seatGrade(seatGrade).build();

		Long basicScreenNum = basicSeat.getScreenNum();
		Integer basicScreenTotalSeat = screenRepo.findById(basicScreenNum).get().getTotalSeat();
		Long updatedScreenNum = updatedScreen.getScreenNum();
		Integer updatedScreenTotalSeat = screenRepo.findById(updatedScreenNum).get().getTotalSeat();

		PrintSeatDTO updatedSeat = seatService.update(seatDTO);

		Integer cBasicScreenTotalSeat = screenRepo.findById(basicScreenNum).get().getTotalSeat();
		Integer cUpdatedScreenTotalSeat = screenRepo.findById(updatedScreenNum).get().getTotalSeat();


		System.out.println("기존 From 스크린 "+basicScreenNum+" 좌석 합 : "+basicScreenTotalSeat);
		System.out.println("기존 To 스크린 "+updatedScreenNum+" 좌석 합 : "+updatedScreenTotalSeat);
		System.out.println("변경 From 스크린 "+basicScreenNum+" 좌석 합 : "+cBasicScreenTotalSeat);
		System.out.println("변경 To 스크린 "+updatedScreenNum+" 좌석 합 : "+cUpdatedScreenTotalSeat);

		if(seatDTO.getScreenNum()==null || seatDTO.getScreenNum().equals(basicScreenNum))
			assert basicScreenTotalSeat.equals(cUpdatedScreenTotalSeat);
		else{
			assert cBasicScreenTotalSeat.equals(basicScreenNum-1);
			assert cUpdatedScreenTotalSeat.equals(updatedScreenTotalSeat+1);
		}
	}

	@Test
	@Transactional
	void findAllByScreenTest() {
		Long screenNum = 1L;

		List<PrintSeatDTO> seat = seatService.findAllByScreenNum(screenNum);
		System.out.println(seat.size());
	}

}
