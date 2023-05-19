package uos.cineseoul.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "PAYMENT_METHOD")
@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class PaymentMethod {
    @Id
    @Column(name="PAMENT_METHOD_CODE", length = 4, nullable = false, unique = true)
    private String paymentMethodCode;

    @Column(name="NAME", nullable = false, unique = true, length = 20)
    private String name ;
}
