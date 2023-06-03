package uos.cineseoul.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertUserDTO;
import uos.cineseoul.dto.request.LoginDTO;
import uos.cineseoul.dto.request.LoginNotMemberDTO;
import uos.cineseoul.dto.response.PrintUserDTO;
import uos.cineseoul.dto.update.UpdateUserDTO;
import uos.cineseoul.entity.User;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.UserMapper;
import uos.cineseoul.repository.UserRepository;
import uos.cineseoul.utils.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        List<User> userList = userRepo.findAll();

        return userList;
    }

    public Page<User> findAll(Pageable pageable) {
        Page<User> userList = userRepo.findAll(pageable);

        return userList;
    }

    public User findOneByNum(Long num) {
        User user = userRepo.findById(num).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ num +"인 사용자가 없습니다.");
        });
        return user;
    }

    public User findOneByPhoneNumAndRole(String phoneNum, UserRole role) {
        User user = userRepo.findByPhoneNumAndRole(phoneNum, role).orElseThrow(()->{
            throw new ResourceNotFoundException("역할이 "+role+"인 해당 전화번호에 대한 사용자가 없습니다.");
        });
        return user;
    }

//    public List<User> findByPhoneNum(String phoneNum) {
//        List<User> userList = userRepo.findByPhoneNum(phoneNum);
////        if(user.isEmpty()){
////            throw new ResourceNotFoundException("해당 전화번호를 가진 사용자 정보가 없습니다.");
////        }
//        return userList;
//    }

    public User findOneById(String id) {
        User user = userRepo.findByUserId(id).orElseThrow(()->{
            throw new ResourceNotFoundException("아이디가 "+id+"인 사용자가 없습니다.");
        });
        return user;
    }

    public User login(@NotNull LoginDTO loginInfo) {
        User user = userRepo.findByUserId(loginInfo.getId()).orElseThrow(() -> {
            throw new ResourceNotFoundException("아이디 또는 비밀번호가 틀렸습니다.");
        });

        // 암호 일치 확인
        if (!passwordEncoder.matches(loginInfo.getPw(), user.getPw())) {
            throw new ResourceNotFoundException("아이디 또는 비밀번호가 틀렸습니다.");
        }

        return user;
    }

    public User loginNotMember(@NotNull LoginNotMemberDTO loginInfo) {
        User user = findOneByPhoneNumAndRole(loginInfo.getPhoneNum(),UserRole.N);
        // 암호 일치 확인
        if (!passwordEncoder.matches(loginInfo.getPw(), user.getPw())) {
            throw new ResourceNotFoundException("비밀번호가 틀렸습니다.");
        }
        return user;
    }

    public void checkDuplicateById(String id){
        if (userRepo.findByUserId(id).isPresent()) {
            throw new DuplicateKeyException("아이디가 존재합니다.");
        }
    }

    public void checkDuplicateByPhoneNum(String phoneNum){
        if (!userRepo.findByPhoneNum(phoneNum).isEmpty()) {
            throw new DuplicateKeyException("해당 전화번호를 가진 회원 또는 비회원이 존재합니다.");
        }
    }

    public boolean checkPassword(String pw, String pwEnc){
        return passwordEncoder.matches(pw, pwEnc);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void deleteByNum(Long num) {
        userRepo.deleteById(num);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public User insert(InsertUserDTO userDTO) {
        User user = UserMapper.INSTANCE.toEntity(userDTO);

        if(user.getRole().equals(UserRole.N)){
            // 비회원
            checkDuplicateByPhoneNum(user.getPhoneNum());
        }else{
            // not 비회원
            checkDuplicateById(user.getId());
            user.setPoint(0);
        }
        user.setPw(passwordEncoder.encode(user.getPw()));
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
