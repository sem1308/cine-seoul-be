package uos.cineseoul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uos.cineseoul.dto.InsertUserDTO;
import uos.cineseoul.dto.UpdateUserDTO;
import uos.cineseoul.entity.User;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.UserMapper;
import uos.cineseoul.repository.UserRepository;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        List<User> users = userRepo.findAll();

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("고객이 없습니다.");
        }

        return users;
    }

    public User login(Map<String, String> loginInfo) {
        User User = userRepo.findByUserId(loginInfo.get("id")).orElseThrow(() -> new ResourceNotFoundException("아이디가 존재하지 않습니다."));

        // 암호 일치 확인
        if (!passwordEncoder.matches(loginInfo.get("pw"), User.getPw())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        return User;
    }

    public void checkDuplicate(String id){
        if (userRepo.findByUserId(id).isPresent()) {
            throw new DuplicateKeyException("아이디가 존재합니다.");
        }
    }

    public User insert(InsertUserDTO UserDTO) {
        User user = UserMapper.INSTANCE.toEntity(UserDTO);

        checkDuplicate(user.getId());

        user.setPw(passwordEncoder.encode(user.getPw()));
        if(!user.getRole().equals("N")){
            user.setPoint(0);
        }

        User newUser = userRepo.save(user);

        return newUser;
    }

    public User update(Long num, UpdateUserDTO userDTO) {

        User user = userRepo.findById(num).orElseThrow(() -> new ResourceNotFoundException("번호가 "+num+"인 고객이 존재하지 않습니다."));
        UserMapper.INSTANCE.updateFromDto(userDTO, user);
        User updatedUser = userRepo.save(user);

        return updatedUser;
    }
}
