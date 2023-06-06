package uos.cineseoul.dto.misc;

import lombok.*;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.utils.enums.AudienceType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class AudienceTypeScheduleSeatDTO {
    @Enumerated(EnumType.STRING)
    AudienceType audienceType;
    ScheduleSeat scheduleSeat;
}
