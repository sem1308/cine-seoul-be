package uos.cineseoul.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.create.CreateScheduleDTO;
import uos.cineseoul.dto.fix.FixScheduleDTO;
import uos.cineseoul.dto.insert.InsertScheduleDTO;
import uos.cineseoul.dto.response.PrintPageDTO;
import uos.cineseoul.dto.response.PrintScheduleDTO;
import uos.cineseoul.dto.response.PrintTicketDTO;
import uos.cineseoul.dto.update.UpdateScheduleDTO;
import uos.cineseoul.entity.Schedule;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.service.ScheduleService;
import uos.cineseoul.service.ScreenService;
import uos.cineseoul.service.movie.MovieService;
import uos.cineseoul.utils.PageUtil;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.enums.StatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController()
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ScreenService screenService;
    private final MovieService movieService;


    @GetMapping()
    @ApiOperation(value = "특정 조건의 상영일정 목록 조회 (filter: movieNum , date, show_seats)", protocols = "http")
    public ResponseEntity<PrintPageDTO<PrintScheduleDTO>> lookUpScheduleListByMovie(@RequestParam(value="movieNum", required = false) Long movieNum,
                                                                @ApiParam(value = "yyyy-MM-dd", required = false) @RequestParam(value="date", required = false) String date,
                                                                @RequestParam(value="show_seats", required = false) boolean isShowSeats,
                                                                @RequestParam(value="sort_order", required = false) boolean isSortOrder,
                                                                @RequestParam(value="sort_dir", required = false) Sort.Direction sortDir,
                                                                @RequestParam(value="page", required = false, defaultValue = "0") int page,
                                                                @RequestParam(value="size", required = false, defaultValue = "12") int size) {
        String sortBy = isSortOrder ? "order" :"schedNum";
        Pageable pageable = PageUtil.setPageable(page, size,sortBy,sortDir);

        Page<Schedule> scheduleList;
        if (movieNum != null && date != null) {
            // 특정 영화와 특정 날짜의 상영일정 조회
            LocalDateTime st = LocalDateTime.of(LocalDate.parse(date), LocalTime.of(0,0,0));
            scheduleList = scheduleService.findByMovieAndDate(movieNum, st, pageable);
        } else if (movieNum != null) {
            // 특정 영화의 상영일정 조회
            scheduleList = scheduleService.findByMovie(movieNum, pageable);
        } else if (date != null) {
            // 특정 날짜의 상영일정 조회
            LocalDateTime st = LocalDateTime.of(LocalDate.parse(date), LocalTime.of(0,0,0));
            scheduleList = scheduleService.findByDate(st, pageable);
        } else{
            // 전체 상영일정 목록 조회
            scheduleList = scheduleService.findAll(pageable);
        }
        List<PrintScheduleDTO> printScheduleDTO = scheduleService.getPrintDTOList(scheduleList.getContent(), isShowSeats);
        return new ResponseEntity<>(new PrintPageDTO<>(printScheduleDTO,scheduleList.getTotalPages()), HttpStatus.OK);
    }

    @GetMapping("/{num}")
    @ApiOperation(value = "상영일정 번호로 조회", protocols = "http")
    public ResponseEntity<PrintScheduleDTO> lookUpScheduleByNum(@PathVariable("num") Long num) {
        Schedule schedule = scheduleService.findOneByNum(num);

        return new ResponseEntity<>(scheduleService.getPrintDTO(schedule, true), HttpStatus.OK);
    }

    @PostMapping("/admin")
    @ApiOperation(value = "상영일정 등록", protocols = "http")
    public ResponseEntity<ReturnMessage<PrintScheduleDTO>> register(@RequestBody CreateScheduleDTO scheduleDTO) {
        InsertScheduleDTO insertScheduleDTO = InsertScheduleDTO.builder().schedTime(scheduleDTO.getSchedTime()).order(scheduleDTO.getOrder())
                                                                        .movie(movieService.findMovie(scheduleDTO.getMovieNum())).screen(screenService.findOneByNum(scheduleDTO.getScreenNum())).build();
        Schedule schedule = scheduleService.insert(insertScheduleDTO);
        ReturnMessage<PrintScheduleDTO> msg = new ReturnMessage<>();
        msg.setMessage("상영일정 등록이 완료되었습니다.");
        msg.setData(scheduleService.getPrintDTO(schedule, false));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping("/admin")
    @ApiOperation(value = "상영일정 정보 변경", protocols = "http")
    public ResponseEntity<ReturnMessage<PrintScheduleDTO>> update(@RequestBody FixScheduleDTO scheduleDTO) {
        Screen screen = null;
        if(scheduleDTO.getScreenNum()!=null){
            screen = screenService.findOneByNum(scheduleDTO.getScreenNum());
        }
        Schedule schedule = scheduleService.update(scheduleDTO.getSchedNum(), UpdateScheduleDTO.builder().schedTime(scheduleDTO.getSchedTime()).order(scheduleDTO.getOrder()).screen(screen).build());
        ReturnMessage<PrintScheduleDTO> msg = new ReturnMessage<>();
        msg.setMessage("상영일정 변경이 완료되었습니다.");
        msg.setData(scheduleService.getPrintDTO(schedule, false));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @DeleteMapping("/admin/{num}")
    @ApiOperation(value = "상영일정 삭제 by 상영일정 번호 (fileter: delete_only_seat - 상영일정-좌석만 삭제)", protocols = "http")
    public ResponseEntity<ReturnMessage<PrintTicketDTO>> delete(@PathVariable("num") Long num, @RequestParam(value = "delete_only_seat", required = false) boolean deleteOnlySeat) {
        ReturnMessage<PrintTicketDTO> msg = new ReturnMessage<>();
        if(deleteOnlySeat){
            scheduleService.deleteScheduleSeat(scheduleService.findOneByNum(num));
            msg.setMessage("상영일정-좌석 삭제가 완료되었습니다.");
        }else{
            scheduleService.delete(num);
            msg.setMessage("상영일정 삭제가 완료되었습니다.");
        }
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}