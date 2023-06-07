package uos.cineseoul.entity;

import lombok.*;
import uos.cineseoul.utils.enums.AudienceType;

import javax.persistence.*;

@Entity(name = "RESERVATION_SEAT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@IdClass(ReservationSeatId.class)
public class ReservationSeat {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_NUM", nullable = false)
    private Ticket ticket;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEAT_NUM", nullable = false)
    private Seat seat;
}