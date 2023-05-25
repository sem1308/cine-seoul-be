package uos.cineseoul.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import uos.cineseoul.entity.PaymentMethod;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.entity.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class InsertPaymentDTO {
    private int price;

    @Size(max = 16, min=16)
    private String cardNum;

    private Long userNum;

    private Long ticketNum;

    private String paymentMethodCode;
}
