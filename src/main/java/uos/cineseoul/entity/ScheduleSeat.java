package uos.cineseoul.entity;

import lombok.*;
import uos.cineseoul.utils.enums.Is;

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

    @Column(name="IS_OCCUPIED", nullable = false, unique = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    @Enumerated(EnumType.STRING)
    private Is isOccupied;

    @Version
    private int version;
}
