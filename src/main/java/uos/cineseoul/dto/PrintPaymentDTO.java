package uos.cineseoul.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import uos.cineseoul.entity.PaymentMethod;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.entity.User;

import javax.persistence.*;
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

    private LocalDateTime createdAt;

    private Long ticketNum;

    private PrintPaymentMethodDTO paymentMethod;
}
