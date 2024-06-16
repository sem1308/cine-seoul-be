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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHED_NUM", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEAT_NUM", nullable = false)
    private Seat seat;

//    @Column(name="IS_OCCUPIED", nullable = false, unique = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
//    @Enumerated(EnumType.STRING)
//    private Is isOccupied;

    @Column(name="state", nullable = false, columnDefinition = "VARCHAR(10) DEFAULT 'AVAILABLE'")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SeatState state = SeatState.AVAILABLE;

    @Column(name="reserved_date_time")
    private LocalDateTime reservedDateTime;

    //=== 비즈니스 로직 ===//
    public void select(){
        this.state = SeatState.SELECTED;
    }

    public void reserve(){
        this.state = SeatState.RESERVED;
        this.reservedDateTime = LocalDateTime.now();
    }

    public void cancel(){
        this.state = SeatState.AVAILABLE;
    }

    public void book(){
        this.state = SeatState.BOOKED;
    }
}
