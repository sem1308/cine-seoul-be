package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.entity.User;
import uos.cineseoul.repository.TicketRepository;
import uos.cineseoul.repository.UserRepository;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@Slf4j
class UserRepoTests {
	@Autowired
	UserRepository userRepo;
	@Test
	void registerTest() {
		User user = User.builder().id("sem1308").pw("1308").name("한수한")
						.residentNum("9902211111111").phoneNum("010XXXXXXXX")
						.point(0).role("A").build();

		User savedUser =userRepo.save(user);

		assert user.getId().equals(savedUser.getId());
	}

	@Test
	void findTest() {
		User user = User.builder().id("sem1308").pw("1308").name("한수한")
				.residentNum("9902211111111").phoneNum("010XXXXXXXX")
				.point(0).role("A").build();

		User savedUser =userRepo.save(user);

		Long userNum  = userRepo.findNumById(savedUser.getId()).orElseThrow(()->{
					throw new RuntimeException("user " + savedUser.getId() + " is not exits");
		});

		assert savedUser.getUserNum().equals(userNum);
	}

}
