package uos.cineseoul.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import uos.cineseoul.dto.insert.InsertUserDTO;
import uos.cineseoul.dto.response.PrintUserDTO;
import uos.cineseoul.dto.update.UpdateUserDTO;
import uos.cineseoul.entity.User;
import uos.cineseoul.service.UserService;
import uos.cineseoul.utils.enums.UserRole;

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
				.residentNum("9902211111111").phoneNum("010XXXXXXXX").role(UserRole.M).build();

		User savedUser = userService.insert(userDTO);
	}

	@Test
	@Transactional
	void updateTest() {
		Long userNum = 1L;
		String pw = "1308111";
		UpdateUserDTO userDTO = UpdateUserDTO.builder().pw(pw).name("한두한")
				.phoneNum("011XXXXXXXX").build();

		User updatedUser = userService.update(userNum,userDTO);

		// 변경된 비밀번호 확인
		System.out.println(updatedUser.getPw());
		assert userService.checkPassword(pw,updatedUser.getPw());
	}

	@Test
	@Transactional
	void findTest() {
		String ID = "sem1308";

		User user = userService.findOneById(ID);
	}

}
