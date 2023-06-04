package uos.cineseoul.dto.create;

import lombok.*;
import uos.cineseoul.dto.insert.InsertTicketDTO;
import uos.cineseoul.dto.insert.InsertReservationDTO;
import uos.cineseoul.dto.misc.SeatTypeDTO;
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
public class CreateTicketSubDTO {
    private Long userNum;

    private Long schedNum;

    private List<SeatTypeDTO> seatTypeDTOS;

    public InsertTicketDTO toInsertDTO(User user){
        return InsertTicketDTO.builder().ticketState(TicketState.N).user(user).stdPrice(0).build();
    }
}
