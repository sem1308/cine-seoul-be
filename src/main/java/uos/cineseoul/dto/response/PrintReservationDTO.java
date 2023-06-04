package uos.cineseoul.dto.response;

import lombok.*;
import uos.cineseoul.utils.enums.AudienceType;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintReservationDTO {
//    private Long ticketScheduleSeatNum;
    private PrintSeatDTO seat;

    private AudienceType audienceType;
}
