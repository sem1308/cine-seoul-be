package uos.cineseoul.dto.misc;

import lombok.Data;
import uos.cineseoul.utils.enums.AudienceType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
@Data
public class SeatTypeDTO {
    Long seatNum;
    @Enumerated(EnumType.STRING)
    AudienceType audienceType = AudienceType.G;
}
