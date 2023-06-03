package uos.cineseoul.dto.insert;

import lombok.*;
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
}
