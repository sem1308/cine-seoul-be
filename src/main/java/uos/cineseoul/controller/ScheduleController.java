package uos.cineseoul.controller;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.insert.InsertScheduleDTO;
import uos.cineseoul.dto.response.PrintScheduleDTO;
import uos.cineseoul.dto.update.UpdateScheduleDTO;
import uos.cineseoul.service.ScheduleService;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.enums.StatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@RestController()
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "전체 상영일정 목록 조회", protocols = "http")
    public ResponseEntity<List<PrintScheduleDTO>> lookUpScheduleList() {
        List<PrintScheduleDTO> scheduleList = scheduleService.findAll();
        return new ResponseEntity<>(scheduleList, HttpStatus.OK);
    }

    @GetMapping("/{num}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "상영일정 상세 조회", protocols = "http")
    public ResponseEntity<PrintScheduleDTO> lookUpScheduleByNum(@PathVariable("num") Long num) {
        PrintScheduleDTO schedule = scheduleService.findOneByNum(num);

        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @GetMapping("/date/{schedTime}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "특정 날짜의 상영일정 조회", protocols = "http")
    public ResponseEntity<List<PrintScheduleDTO>> lookUpScheduleListByDate(@ApiParam(value = "yyyy-MM-dd", required = true) @PathVariable("schedTime") String schedTime) {
        LocalDateTime st = LocalDateTime.of(LocalDate.parse(schedTime), LocalTime.of(0,0,0));
        List<PrintScheduleDTO> scheduleList = scheduleService.findByDate(st);
        return new ResponseEntity<>(scheduleList, HttpStatus.OK);
    }

//    @GetMapping("/movie/{movieNum}")
//    @ResponseStatus(value = HttpStatus.OK)
//    @ApiOperation(value = "특정 영화의 상영일정 조회", protocols = "http")
//    public ResponseEntity<List<PrintScheduleDTO>> lookUpScheduleListByMovie(@PathVariable("movieNum") Long movieNum) {
//        List<PrintScheduleDTO> scheduleList = scheduleService.findByMovie(movieNum);
//        return new ResponseEntity<>(scheduleList, HttpStatus.OK);
//    }

//    @GetMapping("/movie")
//    @ResponseStatus(value = HttpStatus.OK)
//    @ApiOperation(value = "특정 영화와 특정 날짜의 상영일정 조회", protocols = "http")
//    public ResponseEntity<List<PrintScheduleDTO>> lookUpScheduleListByMovieAndDate(@RequestParam(value="movieNum", required = true) Long movieNum,
//                                                                                  @ApiParam(value = "yyyy-MM-dd", required = true) @RequestParam(value="schedTime", required = true) String schedTime) {
//        List<PrintScheduleDTO> scheduleList = scheduleService.findByMovie(movieNum);
//        return new ResponseEntity<>(scheduleList, HttpStatus.OK);
//    }


    @PostMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "상영일정 등록", protocols = "http")
    public ResponseEntity<ReturnMessage> register(@RequestBody InsertScheduleDTO scheduleDTO) {
        PrintScheduleDTO schedule = scheduleService.insert(scheduleDTO);
        ReturnMessage<Long> msg = new ReturnMessage<>();
        msg.setMessage("상영일정 등록이 완료되었습니다.");
        msg.setData(schedule.getSchedNum());
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "상영일정 정보 변경", protocols = "http")
    public ResponseEntity<ReturnMessage> update(@RequestBody UpdateScheduleDTO scheduleDTO) {
        PrintScheduleDTO schedule = scheduleService.update(scheduleDTO);
        ReturnMessage<Long> msg = new ReturnMessage<>();
        msg.setMessage("상영일정 변경이 완료되었습니다.");
        msg.setData(schedule.getSchedNum());
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}