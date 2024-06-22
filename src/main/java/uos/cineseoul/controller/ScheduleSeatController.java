package uos.cineseoul.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.request.SelectScheduleSeatDto;
import uos.cineseoul.service.ScheduleSeatService;

@RestController()
@RequestMapping("/schedule/seat")
@RequiredArgsConstructor
public class ScheduleSeatController {
    private final ScheduleSeatService scheduleSeatService;

    @GetMapping("/select")
    public ResponseEntity<Long> selectSeat(@RequestBody SelectScheduleSeatDto selectScheduleSeatDto){
        Long scheduleSeatNum = scheduleSeatService.selectScheduleSeat(selectScheduleSeatDto.getScheduleNum(), selectScheduleSeatDto.getSeatNum());
        return ResponseEntity.ok(scheduleSeatNum);
    }

    @GetMapping("/select/{scheduleSeatNum}")
    public ResponseEntity<Long> selectSeat(@PathVariable Long scheduleSeatNum){
        scheduleSeatService.selectScheduleSeat(scheduleSeatNum);
        return ResponseEntity.ok(scheduleSeatNum);
    }

    @GetMapping("/select/{scheduleSeatNum}/no")
    public ResponseEntity<Long> selectSeatNo(@PathVariable Long scheduleSeatNum){
        scheduleSeatService.selectScheduleSeatWithoutLocking(scheduleSeatNum);
        return ResponseEntity.ok(scheduleSeatNum);
    }
}