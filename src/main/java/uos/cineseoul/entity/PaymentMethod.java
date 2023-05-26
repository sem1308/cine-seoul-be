package uos.cineseoul.entity;

import lombok.*;
import uos.cineseoul.utils.enums.PaymentMethodType;

import javax.persistence.*;

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
    @Enumerated(EnumType.STRING)
    private PaymentMethodType paymentMethodCode;

    @Column(name="NAME", nullable = false, unique = true, length = 20)
    private String name ;
}
