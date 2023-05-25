package uos.cineseoul.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.InsertPaymentDTO;
import uos.cineseoul.dto.PrintPaymentDTO;
import uos.cineseoul.entity.Payment;
import uos.cineseoul.service.PaymentService;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.StatusEnum;

import java.util.List;

@RestController()
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;
    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "전체 결제내역 목록 조회", protocols = "http")
    public ResponseEntity<List<PrintPaymentDTO>> lookUpPaymentList() {
        List<PrintPaymentDTO> paymentList = paymentService.findAll();
        return new ResponseEntity<>(paymentList, HttpStatus.OK);
    }

    @GetMapping("/{num}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "결제내역 상세 조회", protocols = "http")
    public ResponseEntity<PrintPaymentDTO> lookUpPaymentByNum(@PathVariable("num") Long num) {
        PrintPaymentDTO payment = paymentService.findOneByNum(num);

        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

    @GetMapping("/user/{num}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "사용자의 결제내역 조회", protocols = "http")
    public ResponseEntity<List<PrintPaymentDTO>> lookUpPaymentListByDate(@PathVariable("num") Long num) {
        List<PrintPaymentDTO> paymentList = paymentService.findByUserNum(num);
        return new ResponseEntity<>(paymentList, HttpStatus.OK);
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "결제내역 등록", protocols = "http")
    public ResponseEntity register(@RequestBody InsertPaymentDTO paymentDTO) {
        PrintPaymentDTO payment = paymentService.insert(paymentDTO);
        ReturnMessage<PrintPaymentDTO> msg = new ReturnMessage<>();
        msg.setMessage("결제내역 등록이 완료되었습니다.");
        msg.setData(payment);
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}