package uos.cineseoul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertSeatDTO;
import uos.cineseoul.dto.response.PrintSeatDTO;
import uos.cineseoul.dto.update.UpdateSeatDTO;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.SeatMapper;
import uos.cineseoul.repository.*;
import uos.cineseoul.utils.enums.Is;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepo;
    private final ScreenRepository screenRepo;
    private final ScheduleRepository scheduleRepo;
    private final ScheduleSeatRepository scheduleSeatRepo;
    private final TicketSeatRepository ticketSeatRepo;

    public List<Seat> findAll() {
        List<Seat> seatList = seatRepo.findAll();
        if (seatList.isEmpty()) {
            throw new ResourceNotFoundException("좌석이 없습니다.");
        }
        return seatList;
    }

    public Page<Seat> findAll(Pageable pageable) {
        Page<Seat> seatList = seatRepo.findAll(pageable);
        if (seatList.isEmpty()) {
            throw new ResourceNotFoundException("좌석이 없습니다.");
        }
        return seatList;
    }

    public List<Seat> findAllByScreenNum(Long screenNum){
        List<Seat> seatList = seatRepo.findByScreenNum(screenNum);
        if (seatList.isEmpty()) {
            throw new ResourceNotFoundException("스크린 "+screenNum+"에 좌석이 없습니다.");
        }
        return seatList;
    }

    public Seat findOneByNum(Long num) {
        Seat seat = seatRepo.findById(num).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ num +"인 좌석이 없습니다.");
        });
        return seat;
    }

    public Seat findOneByScreenRowCol(Long screenNum, String row, String col) {
        Seat seat = seatRepo.findByScreenNumAndRowAndCol(screenNum, row ,col).orElseThrow(()->{
            throw new ResourceNotFoundException("좌석이 존재하지 않습니다.");
        });
        return seat;
    }

    public void checkDuplicate(Long screenNum, String row, String col){
        if (seatRepo.findByScreenNumAndRowAndCol(screenNum, row ,col).isPresent()) {
            throw new DuplicateKeyException("좌석이 존재합니다.");
        }
    }

    public void checkSchedule(Long screenNum){
        if (!scheduleRepo.findByScreenNum(screenNum).isEmpty()) {
            throw new DuplicateKeyException("상영일정이 존재하는 상영관에는 좌석을 등록 할 수 없습니다.");
        }
    }

    public void checkScheduleSeat(Seat seat){
        if (!scheduleSeatRepo.findAllBySeat(seat).isEmpty()) {
            throw new DuplicateKeyException("상영일정-좌석이 존재하므로 삭제할 수 없습니다.");
        }
    }

    public void checkTicketSeat(Seat seat){
        if (!ticketSeatRepo.findBySeat(seat).isEmpty()) {
            throw new DuplicateKeyException("티켓-좌석이 존재하므로 삭제할 수 없습니다.");
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Seat insert(InsertSeatDTO seatDTO) {
        // 상영관 불러오기
        Screen screen = seatDTO.getScreen();
        // 좌석 중복 체크
        checkDuplicate(screen.getScreenNum(), seatDTO.getRow(), seatDTO.getCol());

        // 스크린의 총 좌석 값 1 증가
        screen.setTotalSeat(screen.getTotalSeat() + 1);
        screenRepo.save(screen);

        // 좌석 생성
        Seat seat = SeatMapper.INSTANCE.toEntity(seatDTO);
        seat.setScreen(screen);

        Seat newSeat = seatRepo.save(seat);

        return newSeat;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void delete(Seat seat) {
        Screen screen = seat.getScreen();

        // 상영일정-좌석 확인
        checkScheduleSeat(seat);
        // 티켓-좌석 확인
        checkTicketSeat(seat);

        // 스크린의 총 좌석 값 1 감소
        screen.setTotalSeat(screen.getTotalSeat() - 1);
        screenRepo.save(screen);

        seatRepo.delete(seat);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void deleteByNum(Long seatNum) {
        Seat seat = findOneByNum(seatNum);
        delete(seat);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void deleteByScreenRowCol(Long ScreenNum, String row, String col) {
        Seat seat = findOneByScreenRowCol(ScreenNum,row,col);
        delete(seat);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Seat update(Long seatNum, UpdateSeatDTO seatDTO) {
        Seat seat = findOneByNum(seatNum);

        // 좌석 상영관 or 행 or 열 변경
        if(seatDTO.getScreen()!=null && seatDTO.getRow()!=null && seatDTO.getCol()!=null){
            // 중복 자리 확인
            checkDuplicate(seatDTO.getScreen().getScreenNum(), seatDTO.getRow(), seatDTO.getCol());
            // 상영관 교체
            if(seatDTO.getScreen().getScreenNum().equals(seat.getScreen().getScreenNum())){
                // 상영일정 확인
                checkSchedule(seat.getScreen().getScreenNum());

                Screen screenFrom = seat.getScreen();
                Screen screenTo = seatDTO.getScreen();
                // 기존 스크린의 총 좌석 값 1 감소
                screenFrom.setTotalSeat(screenFrom.getTotalSeat() - 1);
                // 변환된 스크린의 총 좌석 값 1 증가
                screenTo.setTotalSeat(screenTo.getTotalSeat() + 1);
                screenRepo.save(screenFrom);
                screenRepo.save(screenTo);
            }
        }

        SeatMapper.INSTANCE.updateFromDto(seatDTO, seat);

        Seat updatedSeat = seatRepo.save(seat);

        return updatedSeat;
    }

    public PrintSeatDTO getPrintDTO(Seat seat){
        PrintSeatDTO seatDTO = SeatMapper.INSTANCE.toDTO(seat);
        seatDTO.setSeatPrice(seatDTO.getSeatGrade().getPrice());
        return seatDTO;
    }

    public List<PrintSeatDTO> getPrintDTOList(List<Seat> seatList){
        List<PrintSeatDTO> pSeatList = new ArrayList<>();
        seatList.forEach(seat -> {
            pSeatList.add(getPrintDTO(seat));
        });
        return pSeatList;
    }
}
