package uos.cineseoul.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.create.CreatePaymentDTO;
import uos.cineseoul.dto.response.PrintPaymentDTO;
import uos.cineseoul.entity.Payment;
import uos.cineseoul.service.PaymentService;
import uos.cineseoul.service.TicketService;
import uos.cineseoul.service.UserService;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.enums.StatusEnum;

import java.util.List;

@RestController()
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;
    private final TicketService ticketService;
    @Autowired
    public PaymentController(PaymentService paymentService, UserService userService, TicketService ticketService) {
        this.paymentService = paymentService;
        this.userService = userService;
        this.ticketService = ticketService;
    }

    @GetMapping()
    @ApiOperation(value = "전체 결제내역 목록 조회", protocols = "http")
    public ResponseEntity<List<PrintPaymentDTO>> lookUpPaymentList() {
        List<Payment> paymentList = paymentService.findAll();
        return new ResponseEntity<>(paymentService.getPrintDTOList(paymentList), HttpStatus.OK);
    }

    @GetMapping("/{num}")
    @ApiOperation(value = "결제내역 상세 조회", protocols = "http")
    public ResponseEntity<PrintPaymentDTO> lookUpPaymentByNum(@PathVariable("num") Long num) {
        Payment payment = paymentService.findOneByNum(num);

        return new ResponseEntity<>(paymentService.getPrintDTO(payment), HttpStatus.OK);
    }

    @GetMapping("/user/{num}")
    @ApiOperation(value = "사용자의 결제내역 조회", protocols = "http")
    public ResponseEntity<List<PrintPaymentDTO>> lookUpPaymentListByDate(@PathVariable("num") Long num) {
        List<Payment> paymentList = paymentService.findByUserNum(num);
        return new ResponseEntity<>(paymentService.getPrintDTOList(paymentList), HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation(value = "결제내역 등록", protocols = "http")
    public ResponseEntity register(@RequestBody CreatePaymentDTO paymentDTO) {
        Payment payment = paymentService.insert(paymentDTO.toInsertDTO(userService.findOneByNum(paymentDTO.getUserNum())
                                                                            ,ticketService.findOneByNum(paymentDTO.getTicketNum())));
        ReturnMessage<PrintPaymentDTO> msg = new ReturnMessage<>();
        msg.setMessage("결제내역 등록이 완료되었습니다.");
        msg.setData(paymentService.getPrintDTO(payment));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}