package uos.cineseoul.dto.insert;

import lombok.*;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.utils.enums.AudienceType;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class InsertReservationDTO {
    private Ticket ticket;

    private ScheduleSeat scheduleSeat;

    private AudienceType audienceType;
}
