package uos.cineseoul.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.CancelCreateSeatDTO;
import uos.cineseoul.dto.create.CreateTicketDTO;
import uos.cineseoul.dto.fix.FixTicketDTO;
import uos.cineseoul.dto.insert.InsertTicketDTO;
import uos.cineseoul.dto.response.PrintTicketDTO;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.service.ScheduleService;
import uos.cineseoul.service.TicketService;
import uos.cineseoul.service.UserService;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.enums.StatusEnum;

import javax.transaction.Transactional;
import java.util.List;

@RestController()
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;
    private final ScheduleService scheduleService;

    @Autowired
    public TicketController(TicketService ticketService,UserService userService, ScheduleService scheduleService) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.scheduleService = scheduleService;
    }

    @GetMapping()
    @ApiOperation(value = "전체 티켓 목록 조회", protocols = "http")
    public ResponseEntity<List<PrintTicketDTO>> lookUpTicketList() {
        List<Ticket> ticketList = ticketService.findAll();
        return new ResponseEntity<>(ticketService.getPrintDTOList(ticketList), HttpStatus.OK);
    }

    @GetMapping("/{num}")
    @ApiOperation(value = "티켓 상세 조회", protocols = "http")
    public ResponseEntity<PrintTicketDTO> lookUpTicketByNum(@PathVariable("num") Long num) {
        Ticket ticket = ticketService.findOneByNum(num);

        return new ResponseEntity<>(ticketService.getPrintDTO(ticket), HttpStatus.OK);
    }

    @GetMapping("/user/{num}")
    @ApiOperation(value = "사용자의 티켓 목록 조회", protocols = "http")
    public ResponseEntity<List<PrintTicketDTO>> lookUpTicketByUserNum(@PathVariable("num") Long num) {
        List<Ticket> ticketList = ticketService.findByUserNum(num);

        return new ResponseEntity<>(ticketService.getPrintDTOList(ticketList), HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation(value = "티켓 등록", protocols = "http")
    public ResponseEntity register(@RequestBody CreateTicketDTO ticketDTO) {
        InsertTicketDTO iTicketDTO = ticketDTO.toInsertDTO(userService.findOneByNum(ticketDTO.getSchedNum()),scheduleService.findScheduleSeat(ticketDTO.getSchedNum(),ticketDTO.getSeatNum()));
        Ticket ticket = ticketService.insert(iTicketDTO);
        ReturnMessage<PrintTicketDTO> msg = new ReturnMessage<>();
        msg.setMessage("티켓 예매가 완료되었습니다.");
        msg.setData(ticketService.getPrintDTO(ticket));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping()
    @ApiOperation(value = "티켓 정보 변경", protocols = "http")
    public ResponseEntity update(@RequestBody FixTicketDTO ticketDTO) {
        Ticket ticket = ticketService.update(ticketDTO.getTicketNum(), ticketDTO.toUpdateDTO());
        ReturnMessage<PrintTicketDTO> msg = new ReturnMessage<>();
        msg.setMessage("티켓 변경이 완료되었습니다.");
        msg.setData(ticketService.getPrintDTO(ticket));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping("/cancel/register")
    @Transactional
    @ApiOperation(value = "티켓 취소 및 재등록", protocols = "http")
    public ResponseEntity cancelAndRegister(@RequestBody CancelCreateSeatDTO ticketDTO) {
        ticketService.update(ticketDTO.getTicketNum(), ticketDTO.toUpdateDTO());
        InsertTicketDTO iTicketDTO = ticketDTO.toInsertDTO(userService.findOneByNum(ticketDTO.getSchedNum()),scheduleService.findScheduleSeat(ticketDTO.getSchedNum(),ticketDTO.getSeatNum()));
        Ticket ticket = ticketService.insert(iTicketDTO);
        ReturnMessage<PrintTicketDTO> msg = new ReturnMessage<>();
        msg.setMessage("티켓 취소 및 재등록이 완료되었습니다.");
        msg.setData(ticketService.getPrintDTO(ticket));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}