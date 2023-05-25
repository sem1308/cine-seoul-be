package uos.cineseoul.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.InsertTicketDTO;
import uos.cineseoul.dto.PrintTicketDTO;
import uos.cineseoul.dto.UpdateTicketDTO;
import uos.cineseoul.service.TicketService;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.StatusEnum;

import java.util.List;

@RestController()
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;
    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "전체 티켓 목록 조회", protocols = "http")
    public ResponseEntity<List<PrintTicketDTO>> lookUpTicketList() {
        List<PrintTicketDTO> ticketList = ticketService.findAll();
        return new ResponseEntity<>(ticketList, HttpStatus.OK);
    }

    @GetMapping("/{num}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "티켓 상세 조회", protocols = "http")
    public ResponseEntity<PrintTicketDTO> lookUpTicketByNum(@PathVariable("num") Long num) {
        PrintTicketDTO ticket = ticketService.findOneByNum(num);

        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @GetMapping("/user/{num}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "사용자의 티켓 목록 조회", protocols = "http")
    public ResponseEntity<List<PrintTicketDTO>> lookUpTicketByUserNum(@PathVariable("num") Long num) {
        List<PrintTicketDTO> ticketList = ticketService.findByUserNum(num);

        return new ResponseEntity<>(ticketList, HttpStatus.OK);
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "티켓 등록", protocols = "http")
    public ResponseEntity register(@RequestBody InsertTicketDTO ticketDTO) {
        PrintTicketDTO ticket = ticketService.insert(ticketDTO);
        ReturnMessage<PrintTicketDTO> msg = new ReturnMessage<>();
        msg.setMessage("티켓 예매가 완료되었습니다.");
        msg.setData(ticket);
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping("/{num}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "티켓 정보 변경", protocols = "http")
    public ResponseEntity update(@RequestBody UpdateTicketDTO ticketDTO) {
        PrintTicketDTO ticket = ticketService.update(ticketDTO);
        ReturnMessage<PrintTicketDTO> msg = new ReturnMessage<>();
        msg.setMessage("티켓 변경이 완료되었습니다.");
        msg.setData(ticket);
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>("update success", HttpStatus.OK);
    }
}