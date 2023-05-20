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
    // 카드 : code = "C000" , name = "card"
    // 계좌 : code = "A000" , name = "account"
    @Id
    @Column(name="PAMENT_METHOD_CODE", length = 4, nullable = false, unique = true)
    private String paymentMethodCode;

    @Column(name="NAME", nullable = false, unique = true, length = 20)
    private String name ;
}
