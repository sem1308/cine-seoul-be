package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import uos.cineseoul.dto.InsertScreenDTO;
import uos.cineseoul.dto.PrintScreenDTO;
import uos.cineseoul.dto.UpdateScreenDTO;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.mapper.ScreenMapper;
import uos.cineseoul.repository.ScreenRepository;
import uos.cineseoul.service.ScreenService;

import javax.transaction.Transactional;

@SpringBootTest
@Slf4j
class ScreenServiceTests {
	@Autowired
	ScreenService screenService;
	@Test
	@Transactional
	@Rollback(false)
	void registerTest() {
		String name = "B";
		InsertScreenDTO screenDTO = InsertScreenDTO.builder().name(name).build();

		PrintScreenDTO savedScreen = screenService.insert(screenDTO);

		assert savedScreen.getName().equals(screenDTO.getName());
	}

	@Test
	@Transactional
	void updateTest() {
		Long screenNum = 1L;
		String name = "D";
		UpdateScreenDTO screenDTO = UpdateScreenDTO.builder().screenNum(screenNum).name(name).build();

		PrintScreenDTO savedScreen = screenService.update(screenDTO);

		assert savedScreen.getName().equals(name);
	}
}
