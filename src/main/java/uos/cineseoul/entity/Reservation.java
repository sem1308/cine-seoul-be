package uos.cineseoul.entity;

import lombok.*;
import uos.cineseoul.utils.enums.AudienceType;

import javax.persistence.*;

@Entity(name = "RESERVATION")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="RESERVATION_NUM")
    private Long reservationNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_NUM", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "SCHED_NUM", referencedColumnName = "SCHED_NUM", nullable = false),
            @JoinColumn(name = "SEAT_NUM", referencedColumnName = "SEAT_NUM", nullable = false)
    })
    private ScheduleSeat scheduleSeat;

    @Column(name="AUDIENCE_TYPE", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    private AudienceType audienceType;
}