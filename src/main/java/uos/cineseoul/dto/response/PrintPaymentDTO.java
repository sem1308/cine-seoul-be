package uos.cineseoul.dto.response;

import lombok.*;
import uos.cineseoul.utils.enums.PayState;
import uos.cineseoul.utils.enums.PaymentMethod;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintPaymentDTO {
    private Long paymentNum;

    private String approvalNum ;

    private int price;

    @Enumerated(EnumType.STRING)
    private PayState state;

    private LocalDateTime createdAt;

    private Long ticketNum;

    private PaymentMethod paymentMethod;
}
