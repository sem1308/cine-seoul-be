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
import uos.cineseoul.entity.User;
import uos.cineseoul.service.ScheduleService;
import uos.cineseoul.service.TicketService;
import uos.cineseoul.service.UserService;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.enums.StatusEnum;
import uos.cineseoul.utils.enums.UserRole;

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
    @ApiOperation(value = "전체 티켓 목록 조회 (filter : userNum)", protocols = "http")
    public ResponseEntity<List<PrintTicketDTO>> lookUpTicketList(@RequestParam(value="userNum", required = false) Long userNum) {
        List<Ticket> ticketList;
        if(userNum!=null){
            ticketList = ticketService.findByUserNum(userNum);
        }else{
            ticketList = ticketService.findAll();
        }
        return new ResponseEntity<>(ticketService.getPrintDTOList(ticketList), HttpStatus.OK);
    }

    @GetMapping("/{num}")
    @ApiOperation(value = "티켓 번호로 조회", protocols = "http")
    public ResponseEntity<PrintTicketDTO> lookUpTicketByNum(@PathVariable("num") Long num) {
        Ticket ticket = ticketService.findOneByNum(num);

        return new ResponseEntity<>(ticketService.getPrintDTO(ticket), HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation(value = "티켓 등록", protocols = "http")
    public ResponseEntity register(@RequestBody CreateTicketDTO ticketDTO) {
        InsertTicketDTO iTicketDTO = ticketDTO.toInsertDTO(userService.findOneByNum(ticketDTO.getUserNum()),scheduleService.findScheduleSeat(ticketDTO.getSchedNum(),ticketDTO.getSeatNum()));
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

    @DeleteMapping("/{num}")
    @ApiOperation(value = "비회원 티켓 삭제 by 티켓 번호", protocols = "http")
    public ResponseEntity delete(@PathVariable("num") Long num) {
        ticketService.delete(num);
        ReturnMessage<PrintTicketDTO> msg = new ReturnMessage<>();
        msg.setMessage("티켓 삭제가 완료되었습니다.");
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping("/cancel/register")
    @Transactional
    @ApiOperation(value = "티켓 취소 및 재등록", protocols = "http")
    public ResponseEntity cancelAndRegister(@RequestBody CancelCreateSeatDTO ticketDTO) {
        InsertTicketDTO iTicketDTO = ticketDTO.toInsertDTO(userService.findOneByNum(ticketDTO.getUserNum()),scheduleService.findScheduleSeat(ticketDTO.getSchedNum(),ticketDTO.getSeatNum()));
        Ticket ticket = ticketService.insert(iTicketDTO);

        if(ticket.getUser().getRole().equals(UserRole.N)){
            ticketService.delete(ticketDTO.getTicketNum());
        }else{
            ticketService.update(ticketDTO.getTicketNum(), ticketDTO.toUpdateDTO());
        }

        ReturnMessage<PrintTicketDTO> msg = new ReturnMessage<>();
        msg.setMessage("티켓 취소 및 재등록이 완료되었습니다.");
        msg.setData(ticketService.getPrintDTO(ticket));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}