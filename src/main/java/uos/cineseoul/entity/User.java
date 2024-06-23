package uos.cineseoul.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import uos.cineseoul.exception.DataInconsistencyException;
import uos.cineseoul.utils.enums.UserRole;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "USERS")
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "POINT", nullable = true, columnDefinition = "INT DEFAULT 0")
    @Builder.Default
    private Integer point = 0;

    @Column(name = "ROLE", nullable = false, columnDefinition = "char(1)")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @CreationTimestamp
    @Column(name="CREATED_DATE", nullable = false)
    private LocalDateTime createdAt;

    public static User mock(){
        return User.builder()
            .id("sem1308")
            .pw("1308")
            .name("한수한")
            .residentNum("9902211111111")
            .phoneNum("010XXXXXXXX")
            .role(UserRole.M)
            .build();
    }

    //=== 비즈니스 로직 ===//
    public void checkPoint(Integer payPoint){
        if(payPoint != null && !payPoint.equals(0)){
            if(this.role.equals(UserRole.N)){
                throw new IllegalArgumentException("비회원은 포인트결제가 안됩니다.");
            }else{
                if(this.point < payPoint) throw new DataInconsistencyException("유저의 포인트가 결제 포인트보다 적습니다.");
                this.point -= payPoint;
            }
        }
    }

    public void addPoint(int price){
        this.point += (int)(price*0.05);
    }

    public void refund(Payment payment){
        if(!this.role.equals(UserRole.N)){
            // 결제 포인트 환불 및 결제해서 얻은 포인트 반환
            this.point = this.point + payment.getPayedPoint() - (int)(payment.getPrice()*0.05);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof User u){
            return this.userNum.equals(u.getUserNum());
        }
        return false;
    }
}
