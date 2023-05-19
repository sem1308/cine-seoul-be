package uos.cineseoul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/user")
public class UserController {

    //private final UserService userService;
    //private final AccountService accountService;
    //private final JwtTokenProvider jwtTokenProvider;

//    @Autowired
//    public UserController(UserService userService, AccountService accountService, JwtTokenProvider jwtTokenProvider) {
//        this.userService = userService;
//        this.accountService = accountService;
//        this.jwtTokenProvider = jwtTokenProvider;
//    }

//    @GetMapping()
//    @ResponseStatus(value = HttpStatus.OK)
//    public List<User> lookUpUserList() {
//        List<User> users = userService.findAll();
//        return users;
//    }
}