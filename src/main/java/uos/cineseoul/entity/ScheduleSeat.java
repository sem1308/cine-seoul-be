package uos.cineseoul.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity(name = "SCHEDULE_SEAT")
@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
@IdClass(ScheduleSeatId.class)
public class ScheduleSeat {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHED_NUM", nullable = false)
    private Schedule schedule;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEAT_NUM", nullable = false)
    private Seat seat;

    @Column(name="OCCUPIED", nullable = false, unique = false, length = 1)
//    @ColumnDefault("N")
    private String occupied;
}
