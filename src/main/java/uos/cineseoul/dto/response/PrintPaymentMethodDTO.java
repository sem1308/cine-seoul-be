package uos.cineseoul.dto.response;

import lombok.*;
import uos.cineseoul.utils.enums.PaymentMethodType;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintPaymentMethodDTO {
    private PaymentMethodType paymentMethodCode;

    private String name;
}
