package uos.cineseoul.dto.create;

import lombok.*;
import uos.cineseoul.dto.insert.InsertPaymentDTO;
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
public class CreatePaymentDTO {
    private Integer price;

    private Integer payedPoint;

    @Size(max = 16, min=16)
    private String cardNum;

    private String accountNum;

    private PaymentMethod paymentMethod;

    private Long userNum;

    private Long ticketNum;
}
