package uos.cineseoul.entity;

import lombok.*;
import javax.persistence.*;

@Entity(name = "TICKET_SCHEDULE_SEAT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@IdClass(TicketScheduleSeatId.class)
public class TicketScheduleSeat {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ticketNum")
    private Ticket ticket;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "SCHED_NUM", referencedColumnName = "SCHED_NUM", nullable = false),
            @JoinColumn(name = "SEAT_NUM", referencedColumnName = "SEAT_NUM", nullable = false)
    })
    private ScheduleSeat scheduleSeat;

    @Column(name="TYPE", nullable = false, columnDefinition = "char(1)")
    private String type;
}