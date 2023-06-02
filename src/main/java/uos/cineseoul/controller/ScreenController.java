package uos.cineseoul.controller;


import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.insert.InsertScreenDTO;
import uos.cineseoul.dto.response.PrintScreenDTO;
import uos.cineseoul.dto.update.UpdateScreenDTO;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.service.ScreenService;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.enums.StatusEnum;

import java.util.List;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/screen")
public class ScreenController {

    private final ScreenService screenService;

    @GetMapping("/admin")
    @ApiOperation(value = "전체 상영관 목록 조회", protocols = "http")
    public ResponseEntity<List<PrintScreenDTO>> lookUpScreenList() {
        List<Screen> screenList = screenService.findAll();
        return new ResponseEntity<>(screenService.getPrintDTOList(screenList), HttpStatus.OK);
    }

    @GetMapping("/admin/{num}")
    @ApiOperation(value = "상영관 번호로 조회", protocols = "http")
    public ResponseEntity<PrintScreenDTO> lookUpScreenByNum(@PathVariable("num") Long num) {
        Screen screen = screenService.findOneByNum(num);

        return new ResponseEntity<>(screenService.getPrintDTO(screen), HttpStatus.OK);
    }

    @PostMapping("/admin")
    @ApiOperation(value = "상영관 등록", protocols = "http")
    public ResponseEntity register(@RequestBody InsertScreenDTO screenDTO) {
        Screen screen = screenService.insert(screenDTO);
        ReturnMessage<PrintScreenDTO> msg = new ReturnMessage<>();
        msg.setMessage("상영관 등록이 완료되었습니다.");
        msg.setData(screenService.getPrintDTO(screen));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping("/admin")
    @ApiOperation(value = "상영관 정보 변경", protocols = "http")
    public ResponseEntity update(@RequestBody UpdateScreenDTO screenDTO) {
        Screen screen = screenService.update(screenDTO);
        ReturnMessage<PrintScreenDTO> msg = new ReturnMessage<>();
        msg.setMessage("상영관 변경이 완료되었습니다.");
        msg.setData(screenService.getPrintDTO(screen));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}