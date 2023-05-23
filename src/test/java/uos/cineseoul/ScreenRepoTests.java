package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.dto.InsertScreenDTO;
import uos.cineseoul.dto.UpdateScreenDTO;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.User;
import uos.cineseoul.mapper.ScreenMapper;
import uos.cineseoul.repository.ScreenRepository;
import uos.cineseoul.repository.UserRepository;

import javax.transaction.Transactional;

@SpringBootTest
@Slf4j
class ScreenRepoTests {
	@Autowired
	ScreenRepository screenRepo;
	@Test
	@Transactional
	void registerTest() {
		String name = "C";
		InsertScreenDTO screenDTO = InsertScreenDTO.builder().name(name).build();
		Screen screen = ScreenMapper.INSTANCE.toEntity(screenDTO);

		Screen savedScreen = screenRepo.save(screen);

		assert savedScreen.getName().equals(screen.getName());
	}

	@Test
	@Transactional
	void updateTest() {
		Long screenNum = 1L;
		String name = "A"; // 원래 A
		UpdateScreenDTO screenDTO = UpdateScreenDTO.builder().screenNum(screenNum).name(name).build();
		Screen screen = screenRepo.findById(screenNum).get();

		ScreenMapper.INSTANCE.updateFromDto(screenDTO, screen);

		Screen savedScreen = screenRepo.save(screen);

		assert savedScreen.getName().equals(name);
	}



	@Test
	void findTest() {
		String name = "A";
		Screen foundScreen  = screenRepo.findByName(name).orElseThrow(()->{
					throw new RuntimeException("screen " + name + " is not exist");
		});

		assert foundScreen.getName().equals(name);
	}

}
