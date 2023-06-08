package uos.cineseoul.dto.create;

import lombok.*;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.utils.enums.AudienceType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class CreateTicketAudienceDTO {
    @NotNull
    @Enumerated(EnumType.STRING)
    private AudienceType audienceType;

    @NotNull
    private Integer count;
}
