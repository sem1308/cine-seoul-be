package uos.cineseoul.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.insert.InsertUserDTO;
import uos.cineseoul.dto.request.LoginDTO;
import uos.cineseoul.dto.response.PrintUserDTO;
import uos.cineseoul.dto.update.UpdateUserDTO;
import uos.cineseoul.entity.User;
import uos.cineseoul.service.UserService;
import uos.cineseoul.utils.JwtTokenProvider;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.enums.StatusEnum;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/admin")
    @ApiOperation(value = "전체 사용자 목록 조회", protocols = "http")
    public List<PrintUserDTO> lookUpUserList() {
        List<User> users = userService.findAll();
        return userService.getPrintDTOList(users);
    }

    @GetMapping("/{num}")
    @ApiOperation(value = "사용자 번호로 조회", protocols = "http")
    public ResponseEntity<PrintUserDTO> lookUpUser(@PathVariable("num") Long num) {
        User user = userService.findOneByNum(num);

        return new ResponseEntity<>(userService.getPrintDTO(user), HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation(value = "사용자 회원가입", protocols = "http")
    public ResponseEntity register(@RequestBody @Valid InsertUserDTO userDTO) {
        User user = userService.insert(userDTO);
        ReturnMessage<PrintUserDTO> msg = new ReturnMessage<>();
        msg.setMessage("회원가입이 완료되었습니다.");
        msg.setData(userService.getPrintDTO(user));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping("/login")
    @ApiOperation(value = "사용자 로그인", protocols = "http")
    public ResponseEntity login(@RequestBody @Valid LoginDTO loginInfo, @RequestParam(value="userNum", required = false) boolean isMember ) {
        User user;
        if(isMember)
            user = userService.login(loginInfo);
        else
            user = userService.loginNotMember(loginInfo);
        ReturnMessage<String> msg = new ReturnMessage<>();
        List<String> roles = new ArrayList<>();
        roles.add(user.getRole().toString());
        String token = jwtTokenProvider.createToken(user.getUserNum(),user.getId()==null?"":user.getId(),
                                                    user.getName()==null?"":user.getName(),roles);
        msg.setMessage("로그인이 완료되었습니다.");
        msg.setStatus(StatusEnum.OK);
        msg.setData(token);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping()
    @ApiOperation(value = "사용자 정보 변경", protocols = "http")
    public ResponseEntity<ReturnMessage> update(@RequestBody UpdateUserDTO userDTO) {
        User user = userService.update(userDTO);
        ReturnMessage<PrintUserDTO> msg = new ReturnMessage<>();
        msg.setMessage("사용자 정보 변경이 완료되었습니다.");
        msg.setData(userService.getPrintDTO(user));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}