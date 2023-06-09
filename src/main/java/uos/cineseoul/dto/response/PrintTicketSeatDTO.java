package uos.cineseoul.dto.response;

import lombok.*;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintTicketSeatDTO {
//    private Long ticketScheduleSeatNum;
    private PrintSeatDTO seat;
}
