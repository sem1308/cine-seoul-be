package uos.cineseoul.dto.insert;

import lombok.*;
import uos.cineseoul.dto.create.CreatePaymentDTO;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.entity.User;
import uos.cineseoul.utils.enums.PayState;
import uos.cineseoul.utils.enums.PaymentMethod;

import javax.validation.constraints.Size;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class InsertPaymentDTO {
    private Integer price;

    private Integer payedPoint;

    @Size(max = 16, min=16)
    private String cardNum;

    private String accountNum;

    private PayState state;

    private PaymentMethod paymentMethod;

    private User user;

    private Ticket ticket;

    public InsertPaymentDTO(CreatePaymentDTO createPaymentDTO){
        this.price = createPaymentDTO.getPrice();
        this.payedPoint = createPaymentDTO.getPayedPoint();
        this.cardNum = createPaymentDTO.getCardNum();
        this.accountNum = createPaymentDTO.getAccountNum();
        this.paymentMethod = createPaymentDTO.getPaymentMethod();
        this.state = PayState.Y;
    }

    public InsertPaymentDTO(CreatePaymentDTO createPaymentDTO, User user, Ticket ticket){
        this.price = createPaymentDTO.getPrice();
        this.payedPoint = createPaymentDTO.getPayedPoint();
        this.cardNum = createPaymentDTO.getCardNum();
        this.accountNum = createPaymentDTO.getAccountNum();
        this.paymentMethod = createPaymentDTO.getPaymentMethod();
        this.state = PayState.Y;
        this.user = user;
        this.ticket = ticket;
    }
}
