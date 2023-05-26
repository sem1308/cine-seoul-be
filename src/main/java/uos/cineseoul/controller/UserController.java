package uos.cineseoul.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.InsertUserDTO;
import uos.cineseoul.dto.PrintTicketDTO;
import uos.cineseoul.dto.PrintUserDTO;
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
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "전체 사용자 목록 조회", protocols = "http")
    public List<PrintUserDTO> lookUpUserList() {
        List<PrintUserDTO> users = userService.findAll();
        return users;
    }

    @GetMapping("/{num}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "사용자 상세 조회", protocols = "http")
    public ResponseEntity<PrintUserDTO> lookUpUser(@PathVariable("num") Long num) {
        PrintUserDTO user = userService.findOneByNum(num);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "사용자 회원가입", protocols = "http")
    public ResponseEntity register(@RequestBody InsertUserDTO userDTO) {
        PrintUserDTO user = userService.insert(userDTO);
        ReturnMessage<PrintUserDTO> msg = new ReturnMessage<>();
        msg.setMessage("회원가입이 완료되었습니다.");
        msg.setData(user);
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping("/login")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "사용자 로그인", protocols = "http")
    public ResponseEntity login(@RequestBody Map<String, String> loginInfo) {
        PrintUserDTO user = userService.login(loginInfo);

        ReturnMessage<String> msg = new ReturnMessage<>();
        msg.setMessage("로그인이 완료되었습니다.");
        msg.setStatus(StatusEnum.OK);

        List<String> roles = new ArrayList<>();
        roles.add(user.getRole());
        String token = jwtTokenProvider.createToken(user.getUserNum(),user.getId(),user.getName(),roles);
        msg.setData(token);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "사용자 정보 변경", protocols = "http")
    public ResponseEntity<ReturnMessage> update(@RequestBody UpdateUserDTO userDTO) {
        PrintUserDTO user = userService.update(userDTO);
        ReturnMessage<PrintUserDTO> msg = new ReturnMessage<>();
        msg.setMessage("사용자 정보 변경이 완료되었습니다.");
        msg.setData(user);
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}