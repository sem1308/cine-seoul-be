package uos.cineseoul.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import uos.cineseoul.utils.enums.Is;
import uos.cineseoul.utils.enums.SeatState;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "SCHEDULE_SEAT")
@AllArgsConstructor()
@NoArgsConstructor()
@Getter
@Builder
public class ScheduleSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_seat_num")
    private Long scheduleSeatNum;

    @Column(name="state", nullable = false, columnDefinition = "VARCHAR(10) DEFAULT 'AVAILABLE'")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SeatState state = SeatState.AVAILABLE;

    @Column(name="reserved_date_time")
    private LocalDateTime reservedDateTime;

    /* Foreign Key */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHED_NUM", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEAT_NUM", nullable = false)
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NUM")
    private User user;

    //=== 비즈니스 로직 ===//
    public void select(User user){
        this.user = user;
        this.state = SeatState.SELECTED;
    }

    public void reserve(User user){
        if(this.state.equals(SeatState.SELECTED) && this.user.equals(user)){
            // 티켓이 선택된 상태이고 선택한 사람과 예매하려는 사람이 동일하다면
            this.state = SeatState.RESERVED;
            this.reservedDateTime = LocalDateTime.now();
        }else{
            throw new IllegalArgumentException("선택자와 티켓 예매자의 정보가 다릅니다.");
        }
    }

    public void cancel(){
        this.state = SeatState.AVAILABLE;
    }

    public void book(){
        this.state = SeatState.BOOKED;
    }
}
