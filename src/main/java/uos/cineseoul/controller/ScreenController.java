package uos.cineseoul.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.InsertScreenDTO;
import uos.cineseoul.dto.PrintScreenDTO;
import uos.cineseoul.dto.UpdateScreenDTO;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.service.ScreenService;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.StatusEnum;

import java.util.List;

@RestController()
@RequestMapping("/screen")
public class ScreenController {

    private final ScreenService screenService;
    @Autowired
    public ScreenController(ScreenService screenService) {
        this.screenService = screenService;
    }

    @GetMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "전체 상영관 목록 조회", protocols = "http")
    public ResponseEntity<List<PrintScreenDTO>> lookUpScreenList() {
        List<PrintScreenDTO> screenList = screenService.findAll();
        return new ResponseEntity<>(screenList, HttpStatus.OK);
    }

    @GetMapping("/{num}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "상영관 상세 조회", protocols = "http")
    public ResponseEntity<PrintScreenDTO> lookUpScreenByNum(@PathVariable("num") Long num) {
        PrintScreenDTO screen = screenService.findOneByNum(num);

        return new ResponseEntity<>(screen, HttpStatus.OK);
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "상영관 등록", protocols = "http")
    public ResponseEntity register(@RequestBody InsertScreenDTO screenDTO) {
        PrintScreenDTO screen = screenService.insert(screenDTO);
        ReturnMessage<PrintScreenDTO> msg = new ReturnMessage<>();
        msg.setMessage("상영관 등록이 완료되었습니다.");
        msg.setData(screen);
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "상영관 정보 변경", protocols = "http")
    public ResponseEntity update(@RequestBody UpdateScreenDTO screenDTO) {
        PrintScreenDTO screen = screenService.update(screenDTO);
        ReturnMessage<PrintScreenDTO> msg = new ReturnMessage<>();
        msg.setMessage("상영관 변경이 완료되었습니다.");
        msg.setData(screen);
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}