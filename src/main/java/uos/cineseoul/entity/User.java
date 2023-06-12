package uos.cineseoul.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import uos.cineseoul.utils.enums.UserRole;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "USERS")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_NUM")
    private Long userNum;

    /* Foreign */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NUM", nullable = false, insertable = false, updatable = false)
    private List<Ticket> tickets;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Builder.Default
    private List<Review> reviewList = new ArrayList<>();
    /* */

    @Column(name="ID", nullable = true, length = 100)
    private String id;

    @Column(name="PW", nullable = false, length = 500)
    private String pw;

    @Column(name="NAME", nullable = false, length = 100)
    private String name;

    @Column(name="RESIDENT_NUM", nullable = true, length = 13)
    private String residentNum;

    @Column(name = "PHONE_NUM", nullable = false, length = 11)
    private String phoneNum;

    @Column(name = "POINT", nullable = true, columnDefinition = "NUMBER(10) DEFAULT 0")
    private Integer point;

    @Column(name = "ROLE", nullable = false, columnDefinition = "char(1)")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @CreationTimestamp
    @Column(name="CREATED_DATE", nullable = false)
    private LocalDateTime createdAt;
}
