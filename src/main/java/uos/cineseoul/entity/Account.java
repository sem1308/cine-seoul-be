package uos.cineseoul.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "ACCOUNT")
@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class Account {
    @Id
    @Column(name="ACCOUNT_NUM")
    private String accountNum;

    @Column(name = "OWNER_NAME", nullable = false)
    private String ownerName;

    @Column(name="BALANCE", nullable = false, unique = false)
    private Integer balance;

    @Column(name="CARD_NUM", nullable = true, unique = true)
    private String cardNum;

    @CreationTimestamp
    @Column(name="CREATED_AT", nullable = false)
    private LocalDateTime createdAt;
}
