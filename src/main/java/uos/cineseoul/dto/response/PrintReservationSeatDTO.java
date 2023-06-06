package uos.cineseoul.dto.response;

import lombok.*;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintReservationSeatDTO {
//    private Long ticketScheduleSeatNum;
    private PrintSeatDTO seat;
}
