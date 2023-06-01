package uos.cineseoul.dto;

import lombok.*;
import uos.cineseoul.dto.insert.InsertTicketDTO;
import uos.cineseoul.dto.update.UpdateTicketDTO;
import uos.cineseoul.entity.Schedule;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.entity.User;
import uos.cineseoul.utils.enums.TicketState;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class CancelCreateSeatDTO {
    private Long ticketNum;

    private Long userNum;

    private Long schedNum;

    private Long seatNum;

    public InsertTicketDTO toInsertDTO(User user, ScheduleSeat scheduleSeat){
        InsertTicketDTO insertDTO = InsertTicketDTO.builder().ticketState(TicketState.N)
                .user(user).scheduleSeat(scheduleSeat).build();

        return insertDTO;
    }

    public UpdateTicketDTO toUpdateDTO(){
        UpdateTicketDTO insertDTO = UpdateTicketDTO.builder().ticketState(TicketState.C).build();
        return insertDTO;
    }
}