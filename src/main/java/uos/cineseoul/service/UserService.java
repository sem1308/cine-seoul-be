package uos.cineseoul.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertUserDTO;
import uos.cineseoul.dto.response.PrintUserDTO;
import uos.cineseoul.dto.update.UpdateUserDTO;
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
        List<User> userList = userRepo.findAll();

        if (userList.isEmpty()) {
            throw new ResourceNotFoundException("사용자가 없습니다.");
        }

        return userList;
    }

    public User findOneByNum(Long num) {
        User user = userRepo.findById(num).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ num +"인 사용자가 없습니다.");
        });
        return user;
    }

    public User findOneById(String id) {
        User user = userRepo.findByUserId(id).orElseThrow(()->{
            throw new ResourceNotFoundException("아이디가 "+id+"인 사용자가 없습니다.");
        });
        return user;
    }

    public User login(@NotNull Map<String, String> loginInfo) {
        User user = userRepo.findByUserId(loginInfo.get("id")).orElseThrow(() -> {
            throw new ResourceNotFoundException("아이디 또는 비밀번호가 틀렸습니다.");
        });

        // 암호 일치 확인
        if (!passwordEncoder.matches(loginInfo.get("pw"), user.getPw())) {
            throw new ResourceNotFoundException("아이디 또는 비밀번호가 틀렸습니다.");
        }

        return user;
    }

    public void checkDuplicate(String id){
        if (userRepo.findByUserId(id).isPresent()) {
            throw new DuplicateKeyException("아이디가 존재합니다.");
        }
    }

    public boolean checkPassword(String pw, String pwEnc){
        return passwordEncoder.matches(pw, pwEnc);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public User insert(InsertUserDTO userDTO) {
        User user = UserMapper.INSTANCE.toEntity(userDTO);

        checkDuplicate(user.getId());

        user.setPw(passwordEncoder.encode(user.getPw()));
        if(!user.getRole().equals("N")){
            user.setPoint(0);
        }

        User newUser = userRepo.save(user);

        return newUser;
    }
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public User update(UpdateUserDTO userDTO) {
        if(userDTO.getUserNum()!=null){
            userDTO.setPw(passwordEncoder.encode(userDTO.getPw()));
        }
        User user = userRepo.findById(userDTO.getUserNum()).orElseThrow(() -> new ResourceNotFoundException("번호가 "+userDTO.getUserNum()+"인 고객이 존재하지 않습니다."));
        UserMapper.INSTANCE.updateFromDto(userDTO, user);
        User updatedUser = userRepo.save(user);

        return updatedUser;
    }

    public PrintUserDTO getPrintDTO(User user){
        return UserMapper.INSTANCE.toDTO(user);
    }

    public List<PrintUserDTO> getPrintDTOList(List<User> userList){
        List<PrintUserDTO> pUserList = new ArrayList<>();
        userList.forEach(user -> {
            pUserList.add(getPrintDTO(user));
        });
        return pUserList;
    }
}
