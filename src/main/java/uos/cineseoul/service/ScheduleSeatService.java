package uos.cineseoul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertScheduleDTO;
import uos.cineseoul.dto.response.PrintScheduleDTO;
import uos.cineseoul.dto.update.UpdateScheduleDTO;
import uos.cineseoul.entity.Schedule;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.exception.ForbiddenException;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.ScheduleMapper;
import uos.cineseoul.repository.MovieRepository;
import uos.cineseoul.repository.ScheduleRepository;
import uos.cineseoul.repository.ScheduleSeatRepository;
import uos.cineseoul.repository.TicketRepository;
import uos.cineseoul.utils.enums.Is;
import uos.cineseoul.utils.enums.SeatState;
import uos.cineseoul.utils.enums.TicketState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleSeatService {
    private final ScheduleSeatRepository scheduleSeatRepo;

    public ScheduleSeat findScheduleSeat(Long schedNum, Long seatNum) {
        return scheduleSeatRepo.findBySchedNumAndSeatNum(schedNum, seatNum).orElseThrow(() ->
            new ResourceNotFoundException("번호가 " + schedNum + "인 상영일정에는 번호가" + seatNum + "인 좌석이 없습니다."));
    }

    public ScheduleSeat findScheduleSeat(Long scheduleSeatNum) {
        return scheduleSeatRepo.findById(scheduleSeatNum).orElseThrow(() ->
            new ResourceNotFoundException("번호가 " +scheduleSeatNum+ "인 상영일정_좌석이 없습니다."));
    }

    public ScheduleSeat findScheduleSeatForUpdate(Long schedNum, Long seatNum) {
        return scheduleSeatRepo.findBySchedNumAndSeatNumForUpdate(schedNum, seatNum).orElseThrow(() ->
            new ResourceNotFoundException("번호가 " + schedNum + "인 상영일정에는 번호가" + seatNum + "인 좌석이 없습니다."));
    }

    public ScheduleSeat findScheduleSeatForUpdate(Long scheduleSeatNum) {
        return scheduleSeatRepo.findByIdForUpdate(scheduleSeatNum).orElseThrow(() ->
            new ResourceNotFoundException("번호가 " +scheduleSeatNum+ "인 상영일정_좌석이 없습니다."));
    }

    public synchronized Long selectScheduleSeat(Long schedNum, Long seatNum){
        ScheduleSeat scheduleSeat = findScheduleSeatForUpdate(schedNum, seatNum);
        if(!scheduleSeat.getState().equals(SeatState.AVAILABLE)){
           throw new IllegalStateException("해당 좌석은 이미 예약된 상태입니다.");
        }

        scheduleSeat.select();
        return scheduleSeat.getScheduleSeatNum();
    }

    public synchronized Long selectScheduleSeat(Long scheduleSeatNum){
        ScheduleSeat scheduleSeat = findScheduleSeatForUpdate(scheduleSeatNum);
        if(!scheduleSeat.getState().equals(SeatState.AVAILABLE)){
            throw new IllegalStateException("해당 좌석은 이미 예약된 상태입니다.");
        }

        scheduleSeat.select();
        return scheduleSeat.getScheduleSeatNum();
    }

    public synchronized Long selectScheduleSeatNoLocking(Long scheduleSeatNum){
        ScheduleSeat scheduleSeat = findScheduleSeat(scheduleSeatNum);
        if(!scheduleSeat.getState().equals(SeatState.AVAILABLE)){
            throw new IllegalStateException("해당 좌석은 이미 예약된 상태입니다.");
        }

        scheduleSeat.select();
        return scheduleSeat.getScheduleSeatNum();
    }

    public void deleteScheduleSeatList(List<ScheduleSeat> scheduleSeatList) {
        scheduleSeatRepo.deleteAll(scheduleSeatList);
    }

    public void insertScheduleSeat(Schedule schedule, Screen screen) {
        List<Seat> seatList = screen.getSeats();
        seatList.forEach(seat -> {
            ScheduleSeat ss = ScheduleSeat.builder().schedule(schedule).seat(seat).build();
            scheduleSeatRepo.save(ss);
        });
    }
}
