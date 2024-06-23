package uos.cineseoul.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.request.SelectScheduleSeatDto;
import uos.cineseoul.service.ScheduleSeatService;
import uos.cineseoul.utils.CustomUserDetails;

import java.security.Principal;

@RestController()
@RequestMapping("/schedule/seat")
@RequiredArgsConstructor
public class ScheduleSeatController {
    private final ScheduleSeatService scheduleSeatService;

//    @GetMapping("/select")
//    public ResponseEntity<Long> selectSeat(Principal principal, @RequestBody SelectScheduleSeatDto selectScheduleSeatDto){
//        if(principal instanceof CustomUserDetails userDetails){
//
//            Long scheduleSeatNum = scheduleSeatService.selectScheduleSeat(selectScheduleSeatDto.getScheduleNum(), selectScheduleSeatDto.getSeatNum());
//            return ResponseEntity.ok(scheduleSeatNum);
//        }else{
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }

    @GetMapping("/select/{scheduleSeatNum}")
    public ResponseEntity<Long> selectSeat(Authentication authentication, @PathVariable Long scheduleSeatNum){
        Object principal = authentication.getPrincipal();
        if(principal instanceof CustomUserDetails userDetails) {
            Long userNum = userDetails.getNum();
            scheduleSeatService.selectScheduleSeat(scheduleSeatNum, userNum);
            return ResponseEntity.ok(scheduleSeatNum);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/select/{scheduleSeatNum}/no")
    public ResponseEntity<Long> selectSeatNo(@PathVariable Long scheduleSeatNum){
        scheduleSeatService.selectScheduleSeatWithoutLocking(scheduleSeatNum);
        return ResponseEntity.ok(scheduleSeatNum);
    }
}