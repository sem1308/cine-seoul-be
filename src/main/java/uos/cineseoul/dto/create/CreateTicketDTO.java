package uos.cineseoul.dto.create;

import lombok.*;
import uos.cineseoul.dto.insert.InsertReservationSeatDTO;
import uos.cineseoul.dto.insert.InsertTicketDTO;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.User;
import uos.cineseoul.utils.enums.AudienceType;
import uos.cineseoul.utils.enums.TicketState;

import java.util.List;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class CreateTicketDTO {
    private Long userNum;

    private Long schedNum;

    private List<Long> seatNumList;

    private List<AudienceType> audienceTypeList;
}
