package uos.cineseoul.dto.create;

import lombok.*;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.utils.enums.AudienceType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class CreateTicketAudienceDTO {
    @Enumerated(EnumType.STRING)
    private AudienceType audienceType;

    private Integer count;
}
