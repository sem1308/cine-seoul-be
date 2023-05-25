package uos.cineseoul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.InsertScheduleDTO;
import uos.cineseoul.dto.PrintScheduleDTO;
import uos.cineseoul.dto.UpdateScheduleDTO;
import uos.cineseoul.entity.*;
import uos.cineseoul.exception.ForbiddenException;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.ScheduleMapper;
import uos.cineseoul.repository.ScheduleRepository;
import uos.cineseoul.repository.ScheduleSeatRepository;
import uos.cineseoul.repository.ScreenRepository;
import uos.cineseoul.repository.SeatRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepo;
    private final ScreenRepository screenRepo;
    private final SeatRepository seatRepo;
    private final ScheduleSeatRepository scheduleSeatRepo;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepo, ScreenRepository screenRepo,ScheduleSeatRepository scheduleSeatRepo,SeatRepository seatRepo) {
        this.scheduleRepo = scheduleRepo;
        this.screenRepo = screenRepo;
        this.scheduleSeatRepo = scheduleSeatRepo;
        this.seatRepo = seatRepo;
    }

    public List<PrintScheduleDTO> findAll() {
        List<Schedule> scheduleList = scheduleRepo.findAll();
        if (scheduleList.isEmpty()) {
            throw new ResourceNotFoundException("상영일정이 없습니다.");
        }

        return getPrintDTOList(scheduleList);
    }

    public PrintScheduleDTO findOneByNum(Long num) {
        Schedule schedule = scheduleRepo.findById(num).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+ num +"인 상영일정이 없습니다.");
        });
        return getPrintDTO(schedule);
    }

    // 그 날에 해당하는 상영일정 불러오기
    public List<PrintScheduleDTO> findByDate(LocalDateTime time) {
        // 오늘의 상영일정 검색
        LocalDateTime startDatetime = LocalDateTime.of(time.toLocalDate(), time.toLocalTime().of(0,0,0));
        LocalDateTime endDatetime = LocalDateTime.of(time.toLocalDate(), time.toLocalTime().of(23,59,59));

        System.out.println("오늘 시작 날짜 : " + startDatetime);
        System.out.println("오늘 끝 날짜 : " + endDatetime);

        List<Schedule> scheduleList = scheduleRepo.findAllBySchedTimeBetween(startDatetime, endDatetime);

        scheduleList.forEach(sched -> {
            System.out.println("오늘 상영일자 : " + sched.getSchedTime());
        });
        return getPrintDTOList(scheduleList);
    }
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void checkDuplicate(LocalDateTime schedTime, Long screenNum){
        if(scheduleRepo.findBySchedTimeAndScreenNum(schedTime, screenNum).isPresent()){
            throw new DuplicateKeyException("해당 상영관과 시간의 상영일정이 존재합니다.");
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public PrintScheduleDTO insert(InsertScheduleDTO scheduleDTO) {
        // 중복 체크
        checkDuplicate(scheduleDTO.getSchedTime(),scheduleDTO.getScreenNum());

        // 엔티티 매핑
        Schedule schedule = ScheduleMapper.INSTANCE.toEntity(scheduleDTO);
        Screen screen = screenRepo.findById(scheduleDTO.getScreenNum()).get();
        Integer emptySeat = screen.getTotalSeat();

        schedule.setScreen(screen);
        schedule.setEmptySeat(emptySeat);

        // 상영일정 저장
        Schedule savedSched = scheduleRepo.save(schedule);

        // 상영일정-좌석에 저장
        List<Seat> seatList = savedSched.getScreen().getSeats();

        seatList.forEach(seat -> {
            ScheduleSeat ss = ScheduleSeat.builder().schedule(savedSched).seat(seat).occupied("N").build();
            scheduleSeatRepo.save(ss);
        });

        return getPrintDTO(savedSched);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public PrintScheduleDTO update(UpdateScheduleDTO scheduleDTO) {
        // 업데이트할 상영일정 불러오기
        Schedule schedule = scheduleRepo.findById(scheduleDTO.getSchedNum()).orElseThrow(()->{
            throw new ResourceNotFoundException("번호가 "+scheduleDTO.getSchedNum()+"인 상영일정이 존재하지 않습니다.");
        });

        // 상영일정 엔티티 매핑
        ScheduleMapper.INSTANCE.updateFromDto(scheduleDTO, schedule);

        // 상영관이 바꼈으면 상영관 교체후 상영일정-좌석 재등록(?) (단, 이미 예약된 티켓이 있다면 그 티켓의 좌석 행,렬이 변경하려는 상영관에도 존재해야함)
        if(scheduleDTO.getScreenNum()!=null && !(scheduleDTO.getScreenNum().equals(schedule.getScreen().getScreenNum()))){
            Screen screen = screenRepo.findById(scheduleDTO.getScreenNum()).orElseThrow(()->{
                throw new ResourceNotFoundException("번호가 "+scheduleDTO.getScreenNum()+"인 상영관이 존재하지 않습니다.");
            });

            // 예약되어있는 자리 처리
            schedule.getScheduleSeats().forEach(ss -> {
                if(ss.getOccupied().equals("Y")){
                    throw new ForbiddenException("이미 예약된 자리가 존재합니다.");
//                Seat seat = seatRepo.findByScreenNumAndRowAndCol(screen.getScreenNum(),ss.getSeat().getRow(),ss.getSeat().getCol()).orElseThrow(()->{
//                    throw new ResourceNotFoundException("변경하려는 상영관에 "+ss.getSeat().getRow()+ss.getSeat().getCol()+"인 좌석이 존재하지 않습니다.");
//                });
//                ss.setSeat(seat);
//                scheduleSeatRepo.save(ss);
                }else{
                    scheduleSeatRepo.delete(ss);
                }
            });

            // 새로운 자리 등록
            List<Seat> seatList = screen.getSeats();
            seatList.forEach(seat->{
                ScheduleSeat ss = ScheduleSeat.builder().schedule(schedule).seat(seat).occupied("N").build();
                scheduleSeatRepo.save(ss);
            });

            schedule.setScreen(screen);
            schedule.setEmptySeat(screen.getTotalSeat());
        }

        // 상영일정 저장
        Schedule savedSched = scheduleRepo.save(schedule);

        return getPrintDTO(savedSched);
    }

    private PrintScheduleDTO getPrintDTO(Schedule schedule){
        return ScheduleMapper.INSTANCE.toDTO(schedule);
    }

    private List<PrintScheduleDTO> getPrintDTOList(List<Schedule> scheduleList){
        List<PrintScheduleDTO> pScheduleList = new ArrayList<>();
        scheduleList.forEach(schedule -> {
            pScheduleList.add(getPrintDTO(schedule));
        });
        return pScheduleList;
    }

}
