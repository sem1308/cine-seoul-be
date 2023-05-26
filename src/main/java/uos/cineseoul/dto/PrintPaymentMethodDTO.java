package uos.cineseoul.dto;

import lombok.*;
import uos.cineseoul.entity.PaymentMethod;
import uos.cineseoul.utils.enums.PaymentMethodType;

import javax.persistence.Column;
import java.time.LocalDateTime;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintPaymentMethodDTO {
    private PaymentMethodType paymentMethodCode;

    private String name;
}
