package uos.cineseoul.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.dto.insert.InsertUserDTO;
import uos.cineseoul.dto.update.UpdateUserDTO;
import uos.cineseoul.entity.User;
import uos.cineseoul.mapper.UserMapper;
import uos.cineseoul.repository.UserRepository;

import javax.transaction.Transactional;

@SpringBootTest
@Slf4j
class UserRepoTests {
	@Autowired
	UserRepository userRepo;
	@Test
	@Transactional
	void registerTest() {

		// mapper test
		InsertUserDTO userDTO = InsertUserDTO.builder().id("sem1308").pw("1308").name("한수한")
				.residentNum("9902211111111").phoneNum("010XXXXXXXX").role("M").build();

		User user = UserMapper.INSTANCE.toEntity(userDTO);
		if(!user.getRole().equals("N")){
			user.setPoint(0);
		}

		// no mapper test
//		User user = User.builder().id("sem1308").pw("1308").name("한수한")
//						.residentNum("9902211111111").phoneNum("010XXXXXXXX")
//						.point(0).role("A").build();

		User savedUser =userRepo.save(user);

		assert user.getId().equals(savedUser.getId());

//		System.out.println(savedUser.getPoint());
//		System.out.println(savedUser.getCreatedAt());
	}

	@Test
	@Transactional
	void updateTest() {
		Long userNum = 1L;
		UpdateUserDTO userDTO = UpdateUserDTO.builder().userNum(userNum).pw("1308111").name("한두한")
				.phoneNum("011XXXXXXXX").build();

		User user = userRepo.findById(userDTO.getUserNum()).get();

		UserMapper.INSTANCE.updateFromDto(userDTO,user);

		User updatedUser =userRepo.save(user);

		assert user.getId().equals(updatedUser.getId());

		System.out.println(updatedUser.getPw());
	}

	@Test
	@Transactional
	void findTest() {
		String ID = "sem1308";

		Long userNum  = userRepo.findNumById(ID).orElseThrow(()->{
					throw new RuntimeException("user " + ID + " is not exist");
		});
	}

}
