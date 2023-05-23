package uos.cineseoul.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "PAYMENT")
@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class Payment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PAYMENT_NUM")
    private Long paymentNum;

    @Column(name="APPROVAL_NUM", nullable = true, unique = false, length = 30)
    private String approvalNum ;

    @Column(name="PRICE", nullable = false, unique = false)
    private int price;

    @CreationTimestamp
    @Column(name="CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    /* Foreign Key */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NUM", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_NUM", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAMENT_METHOD_CODE", nullable = false)
    private PaymentMethod paymentMethod;
    /* */
}
