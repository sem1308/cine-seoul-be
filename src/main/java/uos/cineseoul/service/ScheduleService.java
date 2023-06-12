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
import uos.cineseoul.utils.enums.TicketState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepo;
    private final ScheduleSeatRepository scheduleSeatRepo;
    private final TicketRepository ticketRepo;
    private final MovieRepository movieRepo;

    public List<Schedule> findAll() {
        List<Schedule> scheduleList = scheduleRepo.findAll();
//        if (scheduleList.isEmpty()) {
//            throw new ResourceNotFoundException("상영일정이 없습니다.");
//        }

        return scheduleList;
    }

    public Page<Schedule> findAll(Pageable pageable) {
        Page<Schedule> scheduleList = scheduleRepo.findAll(pageable);
        return scheduleList;
    }

    // 특정 날짜에 해당하는 상영일정 불러오기
    public List<Schedule> findByDate(LocalDateTime time) {
        // 특정 날짜의 상영일정 검색
        LocalDateTime startDatetime = LocalDateTime.of(time.toLocalDate(), time.toLocalTime().of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(time.toLocalDate(), time.toLocalTime().of(23, 59, 59));

        List<Schedule> scheduleList = scheduleRepo.findAllBySchedTimeBetween(startDatetime, endDatetime);
        return scheduleList;
    }

    public Page<Schedule> findByDate(LocalDateTime time, Pageable pageable) {
        // 특정 날짜의 상영일정 검색
        LocalDateTime startDatetime = LocalDateTime.of(time.toLocalDate(), time.toLocalTime().of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(time.toLocalDate(), time.toLocalTime().of(23, 59, 59));

        Page<Schedule> scheduleList = scheduleRepo.findAllBySchedTimeBetween(startDatetime, endDatetime, pageable);
        return scheduleList;
    }

    // 특정 영화및 특정 날짜의 상영일정 불러오기
    public List<Schedule> findByMovieAndDate(Long movieNum, LocalDateTime time) {
        // 특정 날짜의 상영일정 검색
        LocalDateTime startDatetime = LocalDateTime.of(time.toLocalDate(), time.toLocalTime().of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(time.toLocalDate(), time.toLocalTime().of(23, 59, 59));

        List<Schedule> scheduleList = scheduleRepo.findByMovieNumAndDateBetween(startDatetime, endDatetime, movieNum);
        return scheduleList;
    }

    public Page<Schedule> findByMovieAndDate(Long movieNum, LocalDateTime time, Pageable pageable) {
        // 특정 날짜의 상영일정 검색
        LocalDateTime startDatetime = LocalDateTime.of(time.toLocalDate(), time.toLocalTime().of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(time.toLocalDate(), time.toLocalTime().of(23, 59, 59));

        Page<Schedule> scheduleList = scheduleRepo.findByMovieNumAndDateBetween(startDatetime, endDatetime, movieNum, pageable);
        return scheduleList;
    }

    //특정 영화의 상영일정 불러오기
    public List<Schedule> findByMovie(Long movieNum) {
        List<Schedule> scheduleList = scheduleRepo.findByMovieNum(movieNum);
        return scheduleList;
    }

    public Page<Schedule> findByMovie(Long movieNum, Pageable pageable) {
        Page<Schedule> scheduleList = scheduleRepo.findByMovieNum(movieNum, pageable);
        return scheduleList;
    }

    public Schedule findOneByNum(Long num) {
        Schedule schedule = scheduleRepo.findById(num).orElseThrow(() -> {
            throw new ResourceNotFoundException("번호가 " + num + "인 상영일정이 없습니다.");
        });
        return schedule;
    }

    public ScheduleSeat findScheduleSeat(Long schedNum, Long seatNum) {
        ScheduleSeat scheduleSeatList = scheduleSeatRepo.findBySchedNumAndSeatNum(schedNum, seatNum).orElseThrow(() -> {
            throw new ResourceNotFoundException("번호가 " + schedNum + "인 상영일정에는 번호가" + seatNum + "인 좌석이 없습니다.");
        });

        return scheduleSeatList;
    }

    public void checkDuplicate(LocalDateTime schedTime, Long screenNum) {
        if (scheduleRepo.findBySchedTimeAndScreenNum(schedTime, screenNum).isPresent()) {
            throw new DuplicateKeyException("해당 상영관과 시간의 상영일정이 존재합니다.");
        }
    }
    public static boolean isBetween(LocalDateTime time, LocalDateTime time1, LocalDateTime time2) {
        return time.isAfter(time1) && time.isBefore(time2);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Schedule insert(InsertScheduleDTO scheduleDTO) {
        // 중복 체크
        checkDuplicate(scheduleDTO.getSchedTime(), scheduleDTO.getScreen().getScreenNum());
        // 엔티티 매핑
        Schedule schedule = ScheduleMapper.INSTANCE.toEntity(scheduleDTO);
        schedule.setEmptySeat(schedule.getScreen().getTotalSeat());

        checkSchedTime(schedule, 0);

        // 상영일정 저장
        Schedule savedSched = scheduleRepo.save(schedule);

        // 영화 상영중이라 표시
        Movie movie = schedule.getMovie();
        movie.setIsShowing(Is.Y);
        movieRepo.save(movie);

        // 상영일정-좌석 저장
        insertScheduleSeat(savedSched, savedSched.getScreen());

        return savedSched;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Schedule update(Long schedNum, UpdateScheduleDTO scheduleDTO) {
        // 업데이트할 상영일정 불러오기
        Schedule schedule = findOneByNum(schedNum);
        Screen screenTo = scheduleDTO.getScreen();
        // 상영관이 바꼈으면 상영관 교체후 상영일정-좌석 재등록 (단, 이미 예약된 티켓이 있다면 거절)
        if (screenTo != null && !(screenTo.getScreenNum().equals(schedule.getScreen().getScreenNum()))) {
            if(!ticketRepo.findByScheduleAndTicketStateNot(schedule, TicketState.C).isEmpty()) throw new ForbiddenException("예약된 티켓이 존재하므로 변경이 불가합니다.");
            // 기존 상영일정-좌석 삭제
            scheduleSeatRepo.deleteAll(schedule.getScheduleSeats());
            // 빈좌석 재등록
            schedule.setEmptySeat(screenTo.getTotalSeat());
            // 새로운 상영일정-좌석 등록
            insertScheduleSeat(schedule, screenTo);
        }
        // 상영일정 엔티티 매핑
        ScheduleMapper.INSTANCE.updateFromDto(scheduleDTO, schedule);

        if(scheduleDTO.getSchedTime()!=null){
            checkSchedTime(schedule, 1);
        }

        // 상영일정 저장
        Schedule savedSched = scheduleRepo.save(schedule);

        return savedSched;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void checkSchedTime(Schedule schedule, Integer reviseTime){
        // 10분 간격 확인
        Integer interval = 10;

        // 상영 시간의 day에 해당하고 상영 시간보다 빠른 상영 시간에 상영하는 최대 상영일정 가져오기
        LocalDateTime startDatetime = LocalDateTime.of(schedule.getSchedTime().toLocalDate(), schedule.getSchedTime().toLocalTime().of(0, 0, 0));
        Optional<Schedule> schedule1 = scheduleRepo.findTopByMovie_MovieNumAndSchedTimeBetweenOrderBySchedTimeDesc(schedule.getMovie().getMovieNum(), startDatetime,schedule.getSchedTime().minusMinutes(reviseTime));

        if(schedule1.isPresent()){
            Schedule scheduleB = schedule1.get();
            // 상영할 수 있는 시간인지
            if(isBetween(schedule.getSchedTime(), scheduleB.getSchedTime(), scheduleB.getSchedTime().plusMinutes(scheduleB.getMovie().getRunningTime()+interval))){
                // 상영불가 - 상영시간 겹침
                throw new ForbiddenException("상영시간이 다른 상영시간과 겹칩니다. 영화시작 전 10분과 끝나고 10분후 사이에 시간이 있으면 안됩니다.");
            }
            // 상영 가능
            schedule.setOrder(scheduleB.getOrder()+1);
        }else{
            schedule.setOrder(1);
        }

        // 상영 시간의 day에 해당하고 상영 시간보다 느린 상영 시간에 상영하는 최소 상영일정 가져오기
        LocalDateTime endDatetime = LocalDateTime.of(schedule.getSchedTime().toLocalDate(), schedule.getSchedTime().toLocalTime().of(23, 59, 59));
        Optional<Schedule> schedule2 = scheduleRepo.findTopByMovie_MovieNumAndSchedTimeBetweenOrderBySchedTimeAsc(schedule.getMovie().getMovieNum(), schedule.getSchedTime().plusMinutes(reviseTime), endDatetime);

        if(schedule2.isPresent()){
            Schedule scheduleA = schedule2.get();
            // 상영할 수 있는 시간인지
            if(isBetween(scheduleA.getSchedTime(), schedule.getSchedTime(), schedule.getSchedTime().plusMinutes(schedule.getMovie().getRunningTime()+interval))){
                // 상영불가 - 상영시간 겹침
                throw new ForbiddenException("상영시간이 다른 상영시간과 겹칩니다. 영화시작 전 10분과 끝나고 10분후 사이에 시간이 있으면 안됩니다.");
            }
            scheduleA.setOrder(scheduleA.getOrder()+1);
            scheduleRepo.save(scheduleA);
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void setIsShowing(Movie movie) {
        if(scheduleSeatRepo.findAllBySchedule_Movie(movie).isEmpty()){
            movie.setIsShowing(Is.N);
        }
        movieRepo.save(movie);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void setOrder(Schedule schedule){
        LocalDateTime endDatetime = LocalDateTime.of(schedule.getSchedTime().toLocalDate(), schedule.getSchedTime().toLocalTime().of(23, 59, 59));
        Optional<Schedule> schedule1 = scheduleRepo.findTopByMovie_MovieNumAndSchedTimeBetweenOrderBySchedTimeAsc(schedule.getMovie().getMovieNum(), schedule.getSchedTime().plusMinutes(1), endDatetime);

        if(schedule1.isPresent()){
            Schedule scheduleA = schedule1.get();
            scheduleA.setOrder(scheduleA.getOrder()-1);
            scheduleRepo.save(scheduleA);
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void delete(Long schedNum) {
        // 제거할 상영일정 불러오기
        Schedule schedule = findOneByNum(schedNum);
        setOrder(schedule);
        if(ticketRepo.findBySchedule(schedule).isEmpty()){
            // 상영일정 삭제
            try{
                scheduleRepo.delete(schedule);
            }catch (Exception e){
                throw new ForbiddenException("상영일정-좌석 먼저 삭제해야합니다.");
            }
        }else{
            throw new ForbiddenException("티켓을 먼저 삭제해야합니다.");
        }
        setIsShowing(schedule.getMovie());
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void deleteScheduleSeat(Schedule schedule) {
        scheduleSeatRepo.deleteAll(schedule.getScheduleSeats());
        setIsShowing(schedule.getMovie());
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void insertScheduleSeat(Schedule schedule, Screen screen) {
        List<Seat> seatList = screen.getSeats();
        seatList.forEach(seat -> {
            ScheduleSeat ss = ScheduleSeat.builder().schedule(schedule).seat(seat).isOccupied(Is.N).build();
            scheduleSeatRepo.save(ss);
        });
    }

    public PrintScheduleDTO getPrintDTO(Schedule schedule, boolean isShowSeats) {
        PrintScheduleDTO printScheduleDTO = ScheduleMapper.INSTANCE.toDTO(schedule);
        if (isShowSeats) {
            printScheduleDTO.getScheduleSeats().forEach(ss -> {
                ss.getSeat().setSeatPrice(ss.getSeat().getSeatGrade().getPrice());
            });
        } else {
            printScheduleDTO.setScheduleSeats(null);
        }
        return printScheduleDTO;
    }

    public List<PrintScheduleDTO> getPrintDTOList(List<Schedule> scheduleList, boolean isShowSeats) {
        List<PrintScheduleDTO> pScheduleList = new ArrayList<>();
        scheduleList.forEach(schedule -> {
            pScheduleList.add(getPrintDTO(schedule, isShowSeats));
        });
        return pScheduleList;
    }

}
