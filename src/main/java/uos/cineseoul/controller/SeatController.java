package uos.cineseoul.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.InsertSeatDTO;
import uos.cineseoul.dto.PrintSeatDTO;
import uos.cineseoul.dto.UpdateSeatDTO;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.service.SeatService;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.StatusEnum;

import java.util.List;

@RestController()
@RequestMapping("/seat")
public class SeatController {

    private final SeatService seatService;
    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "전체 좌석 목록 조회", protocols = "http")
    public ResponseEntity<List<PrintSeatDTO>> lookUpSeatList() {
        List<PrintSeatDTO> seatList = seatService.findAll();
        return new ResponseEntity<>(seatList, HttpStatus.OK);
    }

    @GetMapping("/{num}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "좌석 상세 조회", protocols = "http")
    public ResponseEntity<PrintSeatDTO> lookUpSeatByNum(@PathVariable("num") Long num) {
        PrintSeatDTO seat = seatService.findOneByNum(num);

        return new ResponseEntity<>(seat, HttpStatus.OK);
    }

    @GetMapping("/screen/{num}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "상영관의 좌석 목록 조회", protocols = "http")
    public ResponseEntity<List<PrintSeatDTO>> lookUpSeatByUserNum(@PathVariable("num") Long num) {
        List<PrintSeatDTO> seatList = seatService.findAllByScreenNum(num);

        return new ResponseEntity<>(seatList, HttpStatus.OK);
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "좌석 등록", protocols = "http")
    public ResponseEntity register(@RequestBody InsertSeatDTO seatDTO) {
        PrintSeatDTO seat = seatService.insert(seatDTO);
        ReturnMessage<PrintSeatDTO> msg = new ReturnMessage<>();
        msg.setMessage("좌석 예매가 완료되었습니다.");
        msg.setData(seat);
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "좌석 정보 변경", protocols = "http")
    public ResponseEntity update(@RequestBody UpdateSeatDTO seatDTO) {
        PrintSeatDTO seat = seatService.update(seatDTO);
        ReturnMessage<PrintSeatDTO> msg = new ReturnMessage<>();
        msg.setMessage("좌석 정보 변경이 완료되었습니다.");
        msg.setData(seat);
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}