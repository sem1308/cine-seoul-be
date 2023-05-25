package uos.cineseoul.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.InsertUserDTO;
import uos.cineseoul.dto.UpdateUserDTO;
import uos.cineseoul.entity.User;
import uos.cineseoul.service.AccountService;
import uos.cineseoul.service.UserService;
import uos.cineseoul.utils.JwtTokenProvider;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.StatusEnum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AccountService accountService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, AccountService accountService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.accountService = accountService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "전체 고객 목록 조회", protocols = "http")
    public List<User> lookUpMemberList() {
        List<User> users = userService.findAll();
        return users;
    }

    @GetMapping("/{num}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "고객 상세 조회", protocols = "http")
    public ResponseEntity<User> lookUpMember(@PathVariable("num") Long num) {
        User user = userService.findOneByNum(num);
        // 반복 참조 제거
        user.getTickets().forEach(ticket -> {
            ticket.setUser(null);
        });

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "고객 회원가입", protocols = "http")
    public ResponseEntity register(@RequestBody InsertUserDTO userDTO) {
        ReturnMessage<User> msg = new ReturnMessage<>();
        User user = userService.insert(userDTO);
        msg.setMessage("회원가입이 완료되었습니다.");
        msg.setData(user);
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping("/login")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "고객 로그인", protocols = "http")
    public ResponseEntity login(@RequestBody Map<String, String> loginInfo) {
        ReturnMessage<String> msg = new ReturnMessage<>();

        User user = userService.login(loginInfo);

        msg.setMessage("로그인이 완료되었습니다.");
        msg.setStatus(StatusEnum.OK);

        List<String> roles = new ArrayList<>();
        roles.add(user.getRole());
        String token = jwtTokenProvider.createToken(user.getUserNum(),user.getId(),user.getName(),roles);
        msg.setData(token);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping("/{num}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "고객 정보 변경", protocols = "http")
    public ResponseEntity update(@RequestBody UpdateUserDTO userDTO) {
        userService.update(userDTO);

        return new ResponseEntity<>("update success", HttpStatus.OK);
    }
}