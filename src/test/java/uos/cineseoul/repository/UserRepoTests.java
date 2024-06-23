package uos.cineseoul.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.dto.insert.InsertUserDTO;
import uos.cineseoul.dto.update.UpdateUserDTO;
import uos.cineseoul.entity.User;
import uos.cineseoul.mapper.UserMapper;
import uos.cineseoul.repository.UserRepository;
import uos.cineseoul.utils.enums.UserRole;

import javax.transaction.Transactional;

@SpringBootTest
@Slf4j
class UserRepoTests {
	@Autowired
	UserRepository userRepo;

	User user;

	@BeforeEach
	public void init(){
		// mapper test
		InsertUserDTO userDTO = InsertUserDTO.builder().id("sem1308").pw("1308").name("한수한")
			.residentNum("9902211111111").phoneNum("010XXXXXXXX").role(UserRole.M).build();

		user = UserMapper.INSTANCE.toEntity(userDTO);
		userRepo.save(user);
	}

	@Test
	@Transactional
	void updateTest() {
		Long userNum = user.getUserNum();
		UpdateUserDTO userDTO = UpdateUserDTO.builder().pw("1308111").name("한두한")
				.phoneNum("011XXXXXXXX").build();

		User user = userRepo.findById(userNum).get();

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
