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
@Slf4j
class UserRepoTests {
	@Autowired
	UserRepository userRepo;
	@Test
	//@Transactional
	void registerTest() {
		User user = User.builder().id("sem1308").pw("1308").name("한수한")
						.residentNum("9902211111111").phoneNum("010XXXXXXXX")
						.point(0).role("A").build();

		User savedUser =userRepo.save(user);

		assert user.getId().equals(savedUser.getId());
	}

	@Test
	@Transactional
	void findTest() {
		String ID = "sem1308";

		Long userNum  = userRepo.findNumById(ID).orElseThrow(()->{
					throw new RuntimeException("user " + ID + " is not exits");
		});
	}

}
