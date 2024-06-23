package uos.cineseoul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.entity.Schedule;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.ScheduleSeatRepository;
import uos.cineseoul.utils.enums.SeatState;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleSeatService {
    private final ScheduleSeatRepository scheduleSeatRepo;
    private final UserService userService;

    public ScheduleSeat findScheduleSeat(Long schedNum, Long seatNum) {
        return scheduleSeatRepo.findBySchedNumAndSeatNum(schedNum, seatNum).orElseThrow(() ->
            new ResourceNotFoundException("번호가 " + schedNum + "인 상영일정에는 번호가" + seatNum + "인 좌석이 없습니다."));
    }

    public ScheduleSeat findScheduleSeat(Long scheduleSeatNum) {
        return scheduleSeatRepo.findById(scheduleSeatNum).orElseThrow(() ->
            new ResourceNotFoundException("번호가 " +scheduleSeatNum+ "인 상영일정_좌석이 없습니다."));
    }

//    public ScheduleSeat findScheduleSeatForUpdate(Long schedNum, Long seatNum) {
//        return scheduleSeatRepo.findBySchedNumAndSeatNumForUpdate(schedNum, seatNum).orElseThrow(() ->
//            new ResourceNotFoundException("번호가 " + schedNum + "인 상영일정에는 번호가" + seatNum + "인 좌석이 없습니다."));
//    }

    public ScheduleSeat findScheduleSeatForUpdate(Long scheduleSeatNum) {
        return scheduleSeatRepo.findByIdForUpdate(scheduleSeatNum).orElseThrow(() ->
            new ResourceNotFoundException("번호가 " +scheduleSeatNum+ "인 상영일정_좌석이 없습니다."));
    }

//    public synchronized Long selectScheduleSeat(Long schedNum, Long seatNum){
//        ScheduleSeat scheduleSeat = findScheduleSeatForUpdate(schedNum, seatNum);
//        if(!scheduleSeat.getState().equals(SeatState.AVAILABLE)){
//           throw new IllegalStateException("해당 좌석은 이미 예약된 상태입니다.");
//        }
//
//        scheduleSeat.select();
//        return scheduleSeat.getScheduleSeatNum();
//    }

    public synchronized Long selectScheduleSeat(Long scheduleSeatNum, Long userNum){
        ScheduleSeat scheduleSeat = findScheduleSeatForUpdate(scheduleSeatNum);
        if(!scheduleSeat.getState().equals(SeatState.AVAILABLE)){
            throw new IllegalStateException("해당 좌석은 이미 예약된 상태입니다.");
        }

        scheduleSeat.select(userService.findOneByNum(userNum));
        return scheduleSeat.getScheduleSeatNum();
    }

    // For TEST
    public synchronized Long selectScheduleSeat(Long scheduleSeatNum){
        ScheduleSeat scheduleSeat = findScheduleSeatForUpdate(scheduleSeatNum);
        if(!scheduleSeat.getState().equals(SeatState.AVAILABLE)){
            throw new IllegalStateException("해당 좌석은 이미 예약된 상태입니다.");
        }

        scheduleSeat.select(null);
        return scheduleSeat.getScheduleSeatNum();
    }

    // For TEST
    public synchronized Long selectScheduleSeatWithoutLocking(Long scheduleSeatNum){
        ScheduleSeat scheduleSeat = findScheduleSeat(scheduleSeatNum);
        if(!scheduleSeat.getState().equals(SeatState.AVAILABLE)){
            throw new IllegalStateException("해당 좌석은 이미 예약된 상태입니다.");
        }

        scheduleSeat.select(null);
        return scheduleSeat.getScheduleSeatNum();
    }

//    public synchronized Long selectScheduleSeatWithoutLocking(Long schedNum, Long seatNum){
//        ScheduleSeat scheduleSeat = findScheduleSeat(schedNum,seatNum);
//        if(!scheduleSeat.getState().equals(SeatState.AVAILABLE)){
//            throw new IllegalStateException("해당 좌석은 이미 예약된 상태입니다.");
//        }
//
//        scheduleSeat.select();
//        return scheduleSeat.getScheduleSeatNum();
//    }

    public void deleteScheduleSeatList(List<ScheduleSeat> scheduleSeatList) {
        scheduleSeatRepo.deleteAll(scheduleSeatList);
    }

    public void deleteScheduleSeat(ScheduleSeat scheduleSeat) {
        scheduleSeatRepo.delete(scheduleSeat);
    }

    public void insertScheduleSeat(Schedule schedule, Screen screen) {
        List<Seat> seatList = screen.getSeats();
        seatList.forEach(seat -> {
            ScheduleSeat ss = ScheduleSeat.builder().schedule(schedule).seat(seat).build();
            scheduleSeatRepo.save(ss);
        });
    }
}
