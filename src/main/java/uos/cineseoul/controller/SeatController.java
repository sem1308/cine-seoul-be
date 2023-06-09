package uos.cineseoul.controller;


import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.create.CreateSeatDTO;
import uos.cineseoul.dto.fix.FixSeatDTO;
import uos.cineseoul.dto.insert.InsertSeatDTO;
import uos.cineseoul.dto.response.PrintSeatDTO;
import uos.cineseoul.dto.update.UpdateSeatDTO;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.service.ScreenService;
import uos.cineseoul.service.SeatService;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.enums.StatusEnum;

import java.util.List;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/seat")
public class SeatController {

    private final SeatService seatService;
    private final ScreenService screenService;


    @GetMapping("/admin")
    @ApiOperation(value = "전체 좌석 목록 조회 (filter : screenNum)", protocols = "http")
    public ResponseEntity<List<PrintSeatDTO>> lookUpSeatList(@RequestParam(value="screenNum", required = false) Long screenNum) {
        List<Seat> seatList;
        if(screenNum!=null){
            seatList = seatService.findAllByScreenNum(screenNum);
        }else{
            seatList = seatService.findAll();
        }
        return new ResponseEntity<>(seatService.getPrintDTOList(seatList), HttpStatus.OK);
    }

    @GetMapping("/admin/{num}")
    @ApiOperation(value = "좌석 번호로 조회", protocols = "http")
    public ResponseEntity<PrintSeatDTO> lookUpSeatByNum(@PathVariable("num") Long num) {
        Seat seat = seatService.findOneByNum(num);

        return new ResponseEntity<>(seatService.getPrintDTO(seat), HttpStatus.OK);
    }

    @DeleteMapping("/admin/{num}")
    @ApiOperation(value = "좌석 번호로 삭제", protocols = "http")
    public ResponseEntity<ReturnMessage<String>> deleteSeatByNum(@PathVariable("num") Long num) {
        seatService.deleteByNum(num);
        ReturnMessage<String> msg = new ReturnMessage<>();
        msg.setMessage("좌석 삭제가 완료되었습니다.");
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @DeleteMapping("/admin")
    @ApiOperation(value = "상영관 번호, 행, 열로 삭제", protocols = "http")
    public ResponseEntity<ReturnMessage<String>> deleteSeatByRowCol(@RequestParam(value="screen_num") Long screenNum ,
                                                                    @RequestParam(value="row") String row ,
                                                                    @RequestParam(value="col") String col) {
        seatService.deleteByScreenRowCol(screenNum, row, col);
        ReturnMessage<String> msg = new ReturnMessage<>();
        msg.setMessage("좌석 삭제가 완료되었습니다.");
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping("/admin")
    @ApiOperation(value = "좌석 등록", protocols = "http")
    public ResponseEntity<ReturnMessage<PrintSeatDTO>> register(@RequestBody CreateSeatDTO seatDTO) {
        InsertSeatDTO insertSeatDTO = new InsertSeatDTO(seatDTO, screenService.findOneByNum(seatDTO.getScreenNum()));
        Seat seat = seatService.insert(insertSeatDTO);
        ReturnMessage<PrintSeatDTO> msg = new ReturnMessage<>();
        msg.setMessage("좌석 등록이 완료되었습니다.");
        msg.setData(seatService.getPrintDTO(seat));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping("/admin")
    @ApiOperation(value = "좌석 정보 변경", protocols = "http")
    public ResponseEntity<ReturnMessage<PrintSeatDTO>> update(@RequestBody FixSeatDTO seatDTO) {
        Screen screen = null;
        if(seatDTO.getScreenNum()!=null){
            screen = screenService.findOneByNum(seatDTO.getScreenNum());
        }
        Seat seat = seatService.update(seatDTO.getSeatNum(), UpdateSeatDTO.builder().row(seatDTO.getRow()).col(seatDTO.getCol()).seatGrade(seatDTO.getSeatGrade()).screen(screen).build());
        ReturnMessage<PrintSeatDTO> msg = new ReturnMessage<>();
        msg.setMessage("좌석 정보 변경이 완료되었습니다.");
        msg.setData(seatService.getPrintDTO(seat));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}