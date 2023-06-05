package uos.cineseoul.entity;

import lombok.*;

import javax.persistence.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ReservationSeatId {
    private Ticket ticket;

    private Long seat;
}