package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.User;
import uos.cineseoul.repository.ScreenRepository;
import uos.cineseoul.repository.UserRepository;

import javax.transaction.Transactional;

@SpringBootTest
@Slf4j
class ScreenRepoTests {
	@Autowired
	ScreenRepository screenRepo;
	@Test
	//@Transactional
	void registerTest() {
		String name = "A";
		Screen screen = Screen.builder().name(name).totalSeat(0).build();

		Screen savedScreen = screenRepo.save(screen);

		assert savedScreen.getName().equals(screen.getName());
	}

	@Test
	void findTest() {
		String name = "A";
		Screen foundScreen  = screenRepo.findByName(name).orElseThrow(()->{
					throw new RuntimeException("screen " + name + " is not exits");
		});

		assert foundScreen.getName().equals(name);
	}

}
