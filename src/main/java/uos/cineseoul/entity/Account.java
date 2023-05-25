package uos.cineseoul.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

//@Entity(name = "ACCOUNT")
@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="BANK_NUM")
    private Long bankNum;

    @Column(name="BALANCE", nullable = false, unique = false)
    private Integer balance;

    @CreationTimestamp
    @Column(name="CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    /* Foreign Key */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NUM", nullable = false)
    private User user;
    /* */
}
