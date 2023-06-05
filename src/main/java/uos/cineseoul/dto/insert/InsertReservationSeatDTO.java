package uos.cineseoul.dto.insert;

import lombok.*;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.entity.Ticket;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class InsertReservationSeatDTO {
    private Ticket ticket;

    private Seat seat;
}
