package uos.cineseoul.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.insert.InsertScheduleDTO;
import uos.cineseoul.dto.response.PrintScheduleDTO;
import uos.cineseoul.dto.update.UpdateScheduleDTO;
import uos.cineseoul.service.ScheduleService;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.enums.StatusEnum;

import java.time.LocalDateTime;
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
    public ResponseEntity<List<PrintScheduleDTO>> lookUpScheduleListByDate(LocalDateTime schedTime) {
        List<PrintScheduleDTO> scheduleList = scheduleService.findByDate(schedTime);
        return new ResponseEntity<>(scheduleList, HttpStatus.OK);
    }

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