package uos.cineseoul.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.create.CreateScheduleDTO;
import uos.cineseoul.dto.fix.FixScheduleDTO;
import uos.cineseoul.dto.response.PrintScheduleDTO;
import uos.cineseoul.dto.response.PrintTicketDTO;
import uos.cineseoul.entity.Schedule;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.service.ScheduleService;
import uos.cineseoul.service.ScreenService;
import uos.cineseoul.service.movie.MovieService;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.enums.StatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ScreenService screenService;
    private final MovieService movieService;
    @Autowired
    public ScheduleController(ScheduleService scheduleService, ScreenService screenService, MovieService movieService) {
        this.scheduleService = scheduleService;
        this.screenService = screenService;
        this.movieService = movieService;
    }

    @GetMapping()
    @ApiOperation(value = "특정 조건의 상영일정 목록 조회 (filter: movieNum , date)", protocols = "http")
    public ResponseEntity<List<PrintScheduleDTO>> lookUpScheduleListByMovie(@RequestParam(value="movieNum", required = false) Long movieNum,
                                                                            @ApiParam(value = "yyyy-MM-dd", required = false) @RequestParam(value="date", required = false) String date) {
        List<Schedule> scheduleList;
        if (movieNum != null && date != null) {
            // 특정 영화와 특정 날짜의 상영일정 조회
            LocalDateTime st = LocalDateTime.of(LocalDate.parse(date), LocalTime.of(0,0,0));
            scheduleList = scheduleService.findByMovieAndDate(movieNum, st);
        } else if (movieNum != null) {
            // 특정 영화의 상영일정 조회
            scheduleList = scheduleService.findByMovie(movieNum);
        } else if (date != null) {
            // 특정 날짜의 상영일정 조회
            LocalDateTime st = LocalDateTime.of(LocalDate.parse(date), LocalTime.of(0,0,0));
            scheduleList = scheduleService.findByDate(st);
        } else{
            // 전체 상영일정 목록 조회
            scheduleList = scheduleService.findAll();
        }
        return new ResponseEntity<>(scheduleService.getPrintDTOList(scheduleList), HttpStatus.OK);
    }

    @GetMapping("/{num}")
    @ApiOperation(value = "상영일정 번호로 조회", protocols = "http")
    public ResponseEntity<PrintScheduleDTO> lookUpScheduleByNum(@PathVariable("num") Long num) {
        Schedule schedule = scheduleService.findOneByNum(num);

        return new ResponseEntity<>(scheduleService.getPrintDTO(schedule), HttpStatus.OK);
    }

    @PostMapping("/admin")
    @ApiOperation(value = "상영일정 등록", protocols = "http")
    public ResponseEntity<ReturnMessage> register(@RequestBody CreateScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleService.insert(scheduleDTO.toInsertDTO(screenService.findOneByNum(scheduleDTO.getScreenNum()),movieService.findMovie(scheduleDTO.getMovieNum())));
        ReturnMessage<Long> msg = new ReturnMessage<>();
        msg.setMessage("상영일정 등록이 완료되었습니다.");
        msg.setData(schedule.getSchedNum());
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping("/admin")
    @ApiOperation(value = "상영일정 정보 변경", protocols = "http")
    public ResponseEntity<ReturnMessage> update(@RequestBody FixScheduleDTO scheduleDTO) {
        Screen screen;
        try{
            screen = screenService.findOneByNum(scheduleDTO.getScreenNum());
        }catch (Exception e){
            screen = null;
        }
        Schedule schedule = scheduleService.update(scheduleDTO.getSchedNum(),scheduleDTO.toUpdateDTO(screen));
        ReturnMessage<Long> msg = new ReturnMessage<>();
        msg.setMessage("상영일정 변경이 완료되었습니다.");
        msg.setData(schedule.getSchedNum());
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @DeleteMapping("/{num}")
    @ApiOperation(value = "상영일정 삭제 by 상영일정 번호", protocols = "http")
    public ResponseEntity delete(@PathVariable("num") Long num) {
        scheduleService.delete(num);
        ReturnMessage<PrintTicketDTO> msg = new ReturnMessage<>();
        msg.setMessage("상영일정 삭제가 완료되었습니다.");
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}