package uos.cineseoul.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.create.CreateSeatDTO;
import uos.cineseoul.dto.fix.FixSeatDTO;
import uos.cineseoul.dto.response.PrintSeatDTO;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.service.ScreenService;
import uos.cineseoul.service.SeatService;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.enums.StatusEnum;

import java.util.List;

@RestController()
@RequestMapping("/seat")
public class SeatController {

    private final SeatService seatService;
    private final ScreenService screenService;
    @Autowired
    public SeatController(SeatService seatService, ScreenService screenService) {
        this.seatService = seatService;
        this.screenService = screenService;
    }

    @GetMapping()
    @ApiOperation(value = "전체 좌석 목록 조회", protocols = "http")
    public ResponseEntity<List<PrintSeatDTO>> lookUpSeatList() {
        List<Seat> seatList = seatService.findAll();
        return new ResponseEntity<>(seatService.getPrintDTOList(seatList), HttpStatus.OK);
    }

    @GetMapping("/{num}")
    @ApiOperation(value = "좌석 상세 조회", protocols = "http")
    public ResponseEntity<PrintSeatDTO> lookUpSeatByNum(@PathVariable("num") Long num) {
        Seat seat = seatService.findOneByNum(num);

        return new ResponseEntity<>(seatService.getPrintDTO(seat), HttpStatus.OK);
    }

    @GetMapping("/screen/{num}")
    @ApiOperation(value = "상영관의 좌석 목록 조회", protocols = "http")
    public ResponseEntity<List<PrintSeatDTO>> lookUpSeatByUserNum(@PathVariable("num") Long num) {
        List<Seat> seatList = seatService.findAllByScreenNum(num);

        return new ResponseEntity<>(seatService.getPrintDTOList(seatList), HttpStatus.OK);
    }

    @PostMapping("/admin")
    @ApiOperation(value = "좌석 등록", protocols = "http")
    public ResponseEntity register(@RequestBody CreateSeatDTO seatDTO) {
        Seat seat = seatService.insert(seatDTO.toInsertDTO(screenService.findOneByNum(seatDTO.getScreenNum())));
        ReturnMessage<PrintSeatDTO> msg = new ReturnMessage<>();
        msg.setMessage("좌석 등록이 완료되었습니다.");
        msg.setData(seatService.getPrintDTO(seat));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping("/admin")
    @ApiOperation(value = "좌석 정보 변경", protocols = "http")
    public ResponseEntity update(@RequestBody FixSeatDTO seatDTO) {
        Screen screen;
        try{
            screen = screenService.findOneByNum(seatDTO.getScreenNum());
        }catch(Exception e){
            screen = null;
        }
        Seat seat = seatService.update(seatDTO.getSeatNum(), seatDTO.toUpdateDTO(screen));
        ReturnMessage<PrintSeatDTO> msg = new ReturnMessage<>();
        msg.setMessage("좌석 정보 변경이 완료되었습니다.");
        msg.setData(seatService.getPrintDTO(seat));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}