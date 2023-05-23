package uos.cineseoul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.InsertScreenDTO;
import uos.cineseoul.dto.UpdateScreenDTO;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.ScreenMapper;
import uos.cineseoul.repository.ScreenRepository;

import java.util.List;

@Service
public class ScreenService {
    private final ScreenRepository screenRepo;

    @Autowired
    public ScreenService(ScreenRepository screenRepo) {
        this.screenRepo = screenRepo;
    }

    public List<Screen> findAll() {
        List<Screen> screens = screenRepo.findAll();
        if (screens.isEmpty()) {
            throw new ResourceNotFoundException("상영관이 없습니다.");
        }
        return screens;
    }

    public Screen findOneByNum(Long num) {
        Screen screen = screenRepo.findById(num).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ num +"인 상영관이 없습니다.");
        });
        return screen;
    }

    public Screen findOneByName(String name) {
        Screen screen = screenRepo.findByName(name).orElseThrow(()->{
            throw new ResourceNotFoundException("상영관 "+ name +"이 없습니다.");
        });
        return screen;
    }

    public void checkDuplicate(String screenName){
        if (screenRepo.findByName(screenName).isPresent()) {
            throw new DuplicateKeyException("상영관 "+ screenName +"이 존재합니다.");
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Screen insert(InsertScreenDTO screenDTO) {
        checkDuplicate(screenDTO.getName());
        Screen screen = ScreenMapper.INSTANCE.toEntity(screenDTO);

        Screen savedScreen = screenRepo.save(screen);

        return savedScreen;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Screen update(UpdateScreenDTO screenDTO) {
        checkDuplicate(screenDTO.getName());
        Screen screen = screenRepo.findById(screenDTO.getScreenNum()).get();
        ScreenMapper.INSTANCE.updateFromDto(screenDTO, screen);

        Screen savedScreen = screenRepo.save(screen);

        return savedScreen;
    }
}
