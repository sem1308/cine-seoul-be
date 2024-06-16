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
public class ScheduleSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_seat_id")
    private Long scheduleSeatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHED_NUM", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEAT_NUM", nullable = false)
    private Seat seat;

    @Column(name="IS_OCCUPIED", nullable = false, unique = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    @Enumerated(EnumType.STRING)
    private Is isOccupied;

    @Version
    private int version;
}
