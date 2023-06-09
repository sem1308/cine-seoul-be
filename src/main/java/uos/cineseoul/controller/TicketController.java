package uos.cineseoul.controller;


import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.complex.CancelRegisterTicketDTO;
import uos.cineseoul.dto.create.CreateTicketDTO;
import uos.cineseoul.dto.fix.FixTicketDTO;
import uos.cineseoul.dto.insert.InsertTicketDTO;
import uos.cineseoul.dto.response.PrintAudienceTypeDTO;
import uos.cineseoul.dto.response.PrintPageDTO;
import uos.cineseoul.dto.response.PrintTicketDTO;
import uos.cineseoul.dto.update.UpdateTicketDTO;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.service.ScheduleService;
import uos.cineseoul.service.TicketService;
import uos.cineseoul.service.UserService;
import uos.cineseoul.utils.JwtTokenProvider;
import uos.cineseoul.utils.PageUtil;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.enums.AudienceType;
import uos.cineseoul.utils.enums.StatusEnum;

import uos.cineseoul.utils.enums.TicketState;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;
    private final ScheduleService scheduleService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/auth")
    @ApiOperation(value = "전체 티켓 목록 조회 (filter : userNum)", protocols = "http")
    public ResponseEntity<PrintPageDTO<PrintTicketDTO>> lookUpTicketList(@RequestParam(value="userNum", required = false) Long userNum,
                                                                         @RequestParam(value="ticket_state", required = false ) TicketState ticketState,
                                                                         @RequestParam(value="sort_created_date", required = false ) boolean isSortCreatedDate,
                                                                         @RequestParam(value="sort_dir", required = false) Sort.Direction sortDir,
                                                                         @RequestParam(value="page", required = false, defaultValue = "0") int page,
                                                                         @RequestParam(value="size", required = false, defaultValue = "12") int size) {
        String sortBy = isSortCreatedDate ? "createdAt" :"ticketNum";
        Pageable pageable = PageUtil.setPageable(page, size,sortBy,sortDir);

        Page<Ticket> ticketList;
        if(userNum!=null){
            if(ticketState!=null){
                ticketList = ticketService.findByUserNumAndTicketState(userNum, ticketState, pageable);
            }else{
                ticketList = ticketService.findByUserNum(userNum, pageable);
            }
        }else{
            if(ticketState!=null){
                ticketList = ticketService.findAllByTicketState(ticketState, pageable);
            }else{
                ticketList = ticketService.findAll(pageable);
            }
        }
        List<PrintTicketDTO> printTicketDTOS = ticketService.toPrintDTOList(ticketList.getContent());
        return new ResponseEntity<>(new PrintPageDTO<>(printTicketDTOS,ticketList.getTotalPages()), HttpStatus.OK);
    }

    @GetMapping("/auth/audience")
    @ApiOperation(value = "관람객 유형 조회", protocols = "http")
    public ResponseEntity<List<PrintAudienceTypeDTO>> lookUpAudienceTypeList() {
        List<PrintAudienceTypeDTO> printAudienceDTOList = new ArrayList<>();
        for (AudienceType audienceType : AudienceType.values()) {
            printAudienceDTOList.add(new PrintAudienceTypeDTO(audienceType));
        }
        return new ResponseEntity<>(printAudienceDTOList, HttpStatus.OK);
    }

    @GetMapping("/auth/{num}")
    @ApiOperation(value = "티켓 번호로 조회", protocols = "http")
    public ResponseEntity<PrintTicketDTO> lookUpTicketByNum(@PathVariable("num") Long num) {
        Ticket ticket = ticketService.findOneByNum(num);

        return new ResponseEntity<>(ticketService.toPrintDTO(ticket), HttpStatus.OK);
    }

    @PostMapping("/auth")
    @ApiOperation(value = "티켓 등록", protocols = "http")
    public ResponseEntity<ReturnMessage<PrintTicketDTO>> register(@RequestHeader(value = "Authorization") String token, @RequestBody CreateTicketDTO ticketDTO) {
        Long userNum = jwtTokenProvider.getClaims(token).get("num", Long.class);
        InsertTicketDTO iTicketDTO = InsertTicketDTO.builder().ticketState(TicketState.N).schedule(scheduleService.findOneByNum(ticketDTO.getSchedNum()))
                                                            .user(userService.findOneByNum(userNum)).stdPrice(ticketDTO.getStdPrice()).build();
        Ticket ticket = ticketService.insert(iTicketDTO,ticketDTO.getSeatNumList(), ticketDTO.getAudienceTypeDTOList());
        ReturnMessage<PrintTicketDTO> msg = new ReturnMessage<>();
        msg.setMessage("티켓 예매가 완료되었습니다.");
        msg.setData(ticketService.toPrintDTO(ticket));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping("/auth")
    @ApiOperation(value = "티켓 정보 변경", protocols = "http")
    public ResponseEntity<ReturnMessage<PrintTicketDTO>> update(@RequestBody FixTicketDTO ticketDTO) {
        Ticket ticket = ticketService.update(ticketDTO.getTicketNum(), UpdateTicketDTO.builder().ticketState(ticketDTO.getTicketState()).salePrice(ticketDTO.getSalePrice()).build());
        ReturnMessage<PrintTicketDTO> msg = new ReturnMessage<>();
        msg.setMessage("티켓 변경이 완료되었습니다.");
        msg.setData(ticketService.toPrintDTO(ticket));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @DeleteMapping("/auth/{num}")
    @ApiOperation(value = "티켓 삭제 by 티켓 번호", protocols = "http")
    public ResponseEntity<ReturnMessage<String>> delete(@PathVariable("num") Long num) {
        ticketService.deleteByNum(num);
        ReturnMessage<String> msg = new ReturnMessage<>();
        msg.setMessage("티켓 삭제가 완료되었습니다.");
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping("/auth/cancelregister")
    @ApiOperation(value = "티켓 취소 및 등록", protocols = "http")
    public ResponseEntity<ReturnMessage<PrintTicketDTO>> CancelAndRegister(@RequestHeader(value = "Authorization") String token, @RequestBody CancelRegisterTicketDTO ticketDTO) {
        Long userNum = jwtTokenProvider.getClaims(token).get("num", Long.class);
        InsertTicketDTO insertTicketDTO = InsertTicketDTO.builder().ticketState(TicketState.N).stdPrice(ticketDTO.getStdPrice()).schedule(scheduleService.findOneByNum(ticketDTO.getSchedNum()))
                                                        .user(userService.findOneByNum(userNum)).build();
        UpdateTicketDTO updateTicketDTO = UpdateTicketDTO.builder().ticketState(TicketState.C).build();
        Ticket ticket = ticketService.cancelAndChangeSeat(ticketDTO.getTicketNum(),insertTicketDTO, updateTicketDTO, ticketDTO.getSeatNumList(), ticketDTO.getCreateTicketAudienceDTOList());
        ReturnMessage<PrintTicketDTO> msg = new ReturnMessage<>();
        msg.setMessage("티켓 취소 및 등록이 완료되었습니다.");
        msg.setData(ticketService.toPrintDTO(ticket));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}