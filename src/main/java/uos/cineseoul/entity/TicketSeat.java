package uos.cineseoul.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "TICKET_SEAT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@IdClass(TicketSeatId.class)
public class TicketSeat {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_NUM", nullable = false)
    private Ticket ticket;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEAT_NUM", nullable = false)
    private Seat seat;
}