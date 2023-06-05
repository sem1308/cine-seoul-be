package uos.cineseoul.dto.insert;

import lombok.*;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.utils.enums.AudienceType;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class InsertTicketAudienceDTO {
    private Ticket ticket;

    private AudienceType audienceType;
}
