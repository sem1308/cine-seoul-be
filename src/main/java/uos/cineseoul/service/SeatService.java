package uos.cineseoul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.InsertSeatDTO;
import uos.cineseoul.dto.UpdateSeatDTO;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.SeatMapper;
import uos.cineseoul.repository.ScreenRepository;
import uos.cineseoul.repository.SeatRepository;

import java.util.List;
import java.util.Map;

@Service
public class SeatService {
    private final SeatRepository seatRepo;
    private final ScreenRepository screenRepo;

    @Autowired
    public SeatService(SeatRepository seatRepo, ScreenRepository screenRepo) {
        this.seatRepo = seatRepo;
        this.screenRepo = screenRepo;
    }

    public List<Seat> findAll() {
        List<Seat> seats = seatRepo.findAll();
        if (seats.isEmpty()) {
            throw new ResourceNotFoundException("좌석이 없습니다.");
        }
        return seats;
    }

    public List<Seat> findAllByScreenNum(Long screenNum){
        List<Seat> seats = seatRepo.findByScreenNum(screenNum);
        if (seats.isEmpty()) {
            throw new ResourceNotFoundException("스크린 "+screenNum+"에 좌석이 없습니다.");
        }
        return seats;
    }

    public Seat findOneByNum(Long num) {
        Seat seat = seatRepo.findById(num).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ num +"인 좌석이 없습니다.");
        });
        return seat;
    }

    public void checkDuplicate(Long screenNum, String row, String col){
        if (seatRepo.findByScreenNumAndRowAndCol(screenNum, row ,col).isPresent()) {
            throw new DuplicateKeyException("좌석이 존재합니다.");
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Seat insert(InsertSeatDTO seatDTO) {
        checkDuplicate(seatDTO.getScreenNum(), seatDTO.getRow(), seatDTO.getCol());

        // 상영관 불러오기
        Screen screen = screenRepo.findById(seatDTO.getScreenNum()).get();

        // 스크린의 총 좌석 값 1 증가
        screen.setTotalSeat(screen.getTotalSeat() + 1);
        screenRepo.save(screen);

        // 좌석 생성
        Seat seat = SeatMapper.INSTANCE.toEntity(seatDTO);
        seat.setScreen(screen);

        Seat newSeat = seatRepo.save(seat);

        return newSeat;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Seat update(UpdateSeatDTO seatDTO) {
        checkDuplicate(seatDTO.getScreenNum(), seatDTO.getRow(), seatDTO.getCol());

        Seat seat = seatRepo.findById(seatDTO.getSeatNum()).orElseThrow(() -> new ResourceNotFoundException("번호가 "+seatDTO.getSeatNum()+"인 좌석이 존재하지 않습니다."));
        SeatMapper.INSTANCE.updateFromDto(seatDTO, seat);
        // 상영관 교체
        if(seatDTO.getScreenNum()!=null){
            Screen screenFrom = seat.getScreen();
            Screen screenTo = screenRepo.findById(seatDTO.getScreenNum()).get();
            seat.setScreen(screenTo);
            // 기존 스크린의 총 좌석 값 1 감소
            screenFrom.setTotalSeat(screenFrom.getTotalSeat() - 1);
            // 변환된 스크린의 총 좌석 값 1 증가
            screenTo.setTotalSeat(screenTo.getTotalSeat() + 1);
            screenRepo.save(screenFrom);
            screenRepo.save(screenTo);
        }

        Seat updatedSeat = seatRepo.save(seat);

        return updatedSeat;
    }
}
