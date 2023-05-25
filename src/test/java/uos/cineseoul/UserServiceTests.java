package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import uos.cineseoul.dto.InsertUserDTO;
import uos.cineseoul.dto.PrintUserDTO;
import uos.cineseoul.dto.UpdateUserDTO;
import uos.cineseoul.entity.User;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.UserMapper;
import uos.cineseoul.repository.UserRepository;
import uos.cineseoul.service.UserService;

import javax.transaction.Transactional;

@SpringBootTest
@Slf4j
class UserServiceTests {
	@Autowired
	UserService userService;
	@Test
	@Transactional
	@Rollback(false)
	void registerTest() {

		InsertUserDTO userDTO = InsertUserDTO.builder().id("sem1308").pw("1308").name("한수한")
				.residentNum("9902211111111").phoneNum("010XXXXXXXX").role("M").build();

		PrintUserDTO savedUser = userService.insert(userDTO);
	}

	@Test
	@Transactional
	void updateTest() {
		Long userNum = 1L;
		String pw = "1308111";
		UpdateUserDTO userDTO = UpdateUserDTO.builder().userNum(userNum).pw(pw).name("한두한")
				.phoneNum("011XXXXXXXX").build();

		PrintUserDTO updatedUser = userService.update(userDTO);

		// 변경된 비밀번호 확인
		System.out.println(updatedUser.getPw());
		assert userService.checkPassword(pw,updatedUser.getPw());
	}

	@Test
	@Transactional
	void findTest() {
		String ID = "sem1308";

		PrintUserDTO user = userService.findOneById(ID);
	}

}
