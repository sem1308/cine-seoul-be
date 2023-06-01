package uos.cineseoul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertScheduleDTO;
import uos.cineseoul.dto.response.PrintScheduleDTO;
import uos.cineseoul.dto.update.UpdateScheduleDTO;
import uos.cineseoul.entity.*;
import uos.cineseoul.exception.ForbiddenException;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.ScheduleMapper;
import uos.cineseoul.repository.ScheduleRepository;
import uos.cineseoul.repository.ScheduleSeatRepository;
import uos.cineseoul.repository.ScreenRepository;
import uos.cineseoul.repository.SeatRepository;
import uos.cineseoul.utils.enums.Is;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepo;
    private final ScheduleSeatRepository scheduleSeatRepo;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepo, ScheduleSeatRepository scheduleSeatRepo) {
        this.scheduleRepo = scheduleRepo;
        this.scheduleSeatRepo = scheduleSeatRepo;
    }

    public List<Schedule> findAll() {
        List<Schedule> scheduleList = scheduleRepo.findAll();
        if (scheduleList.isEmpty()) {
            throw new ResourceNotFoundException("상영일정이 없습니다.");
        }

        return scheduleList;
    }

    public ScheduleSeat findScheduleSeat(Long schedNum, Long seatNum) {
        ScheduleSeat scheduleSeatList = scheduleSeatRepo.findBySchedNumAndSeatNum(schedNum,seatNum).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ schedNum +"인 상영일정에는 번호가"+seatNum+"인 좌석이 없습니다.");
        });

        return scheduleSeatList;
    }

    public Schedule findOneByNum(Long num) {
        Schedule schedule = scheduleRepo.findById(num).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ num +"인 상영일정이 없습니다.");
        });
        return schedule;
    }

    // 특정 날짜에 해당하는 상영일정 불러오기
    public List<Schedule> findByDate(LocalDateTime time) {
        // 특정 날짜의 상영일정 검색
        LocalDateTime startDatetime = LocalDateTime.of(time.toLocalDate(), time.toLocalTime().of(0,0,0));
        LocalDateTime endDatetime = LocalDateTime.of(time.toLocalDate(), time.toLocalTime().of(23,59,59));

        System.out.println("오늘 시작 날짜 : " + startDatetime);
        System.out.println("오늘 끝 날짜 : " + endDatetime);

        List<Schedule> scheduleList = scheduleRepo.findAllBySchedTimeBetween(startDatetime, endDatetime);

        scheduleList.forEach(sched -> {
            System.out.println("오늘 상영일자 : " + sched.getSchedTime());
        });
        return scheduleList;
    }

    // 특정 영화및 특정 날짜의 상영일정 불러오기
    public List<Schedule> findByMovieAndDate(Long movieNum, LocalDateTime time) {
        // 특정 날짜의 상영일정 검색
        LocalDateTime startDatetime = LocalDateTime.of(time.toLocalDate(), time.toLocalTime().of(0,0,0));
        LocalDateTime endDatetime = LocalDateTime.of(time.toLocalDate(), time.toLocalTime().of(23,59,59));

        List<Schedule> scheduleList = scheduleRepo.findByMovieNumAndDateBetween(startDatetime, endDatetime, movieNum);
        return scheduleList;
    }

    //특정 영화의 상영일정 불러오기
    public List<Schedule> findByMovie(Long movieNum) {
        List<Schedule> scheduleList = scheduleRepo.findByMovieNum(movieNum);
        return scheduleList;
    }

    public void checkDuplicate(LocalDateTime schedTime, Long screenNum){
        if(scheduleRepo.findBySchedTimeAndScreenNum(schedTime, screenNum).isPresent()){
            throw new DuplicateKeyException("해당 상영관과 시간의 상영일정이 존재합니다.");
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Schedule insert(InsertScheduleDTO scheduleDTO) {
        // 중복 체크
        checkDuplicate(scheduleDTO.getSchedTime(),scheduleDTO.getScreen().getScreenNum());

        // 엔티티 매핑
        Schedule schedule = ScheduleMapper.INSTANCE.toEntity(scheduleDTO);
        Integer emptySeat = schedule.getScreen().getTotalSeat();

        schedule.setEmptySeat(emptySeat);

        // 상영일정 저장
        Schedule savedSched = scheduleRepo.save(schedule);

        // 상영일정-좌석에 저장
        List<Seat> seatList = savedSched.getScreen().getSeats();

        seatList.forEach(seat -> {
            ScheduleSeat ss = ScheduleSeat.builder().schedule(savedSched).seat(seat).isOccupied(Is.N).build();
            scheduleSeatRepo.save(ss);
        });

        return savedSched;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Schedule update(Long schedNum, UpdateScheduleDTO scheduleDTO) {
        // 업데이트할 상영일정 불러오기
        Schedule schedule = findOneByNum(schedNum);

        Screen screen = scheduleDTO.getScreen();
        // 상영관이 바꼈으면 상영관 교체후 상영일정-좌석 재등록 (단, 이미 예약된 티켓이 있다면 거절)
        if(screen!=null && !(screen.getScreenNum().equals(schedule.getScreen().getScreenNum()))){

            // 예약되어있는 자리 처리
            schedule.getScheduleSeats().forEach(ss -> {
                if(ss.getIsOccupied().equals(Is.Y)){
                    throw new ForbiddenException("이미 예약된 자리가 존재합니다.");
                }else{
                    scheduleSeatRepo.delete(ss);
                }
            });

            // 새로운 자리 등록
            List<Seat> seatList = screen.getSeats();
            seatList.forEach(seat->{
                ScheduleSeat ss = ScheduleSeat.builder().schedule(schedule).seat(seat).isOccupied(Is.N).build();
                scheduleSeatRepo.save(ss);
            });
            schedule.setEmptySeat(screen.getTotalSeat());
        }
        // 상영일정 엔티티 매핑
        ScheduleMapper.INSTANCE.updateFromDto(scheduleDTO, schedule);

        // 상영일정 저장
        Schedule savedSched = scheduleRepo.save(schedule);

        return savedSched;
    }

    public PrintScheduleDTO getPrintDTO(Schedule schedule){
        PrintScheduleDTO printScheduleDTO = ScheduleMapper.INSTANCE.toDTO(schedule);
        printScheduleDTO.getScheduleSeats().forEach(ss->{
            ss.getSeat().setSeatPrice(ss.getSeat().getSeatGrade().getPrice());
        });
        return printScheduleDTO;
    }

    public List<PrintScheduleDTO> getPrintDTOList(List<Schedule> scheduleList){
        List<PrintScheduleDTO> pScheduleList = new ArrayList<>();
        scheduleList.forEach(schedule -> {
            pScheduleList.add(getPrintDTO(schedule));
        });
        return pScheduleList;
    }

}
