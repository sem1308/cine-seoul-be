package uos.cineseoul.dto.insert;

import lombok.*;
import uos.cineseoul.utils.enums.PaymentMethodType;

import javax.validation.constraints.Size;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class InsertPaymentDTO {
    private int price;

    @Size(max = 16, min=16)
    private String cardNum;

    private String accountNum;

    private Long userNum;

    private Long ticketNum;

    private PaymentMethodType paymentMethodCode;
}
