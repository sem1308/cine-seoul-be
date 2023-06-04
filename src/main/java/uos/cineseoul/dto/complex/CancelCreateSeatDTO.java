package uos.cineseoul.dto.complex;

import lombok.*;
import uos.cineseoul.dto.insert.InsertTicketDTO;
import uos.cineseoul.dto.update.UpdateTicketDTO;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.User;
import uos.cineseoul.utils.enums.TicketState;

import java.util.List;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class CancelCreateSeatDTO {
    private Long ticketNum;

    private Long userNum;

    private Long schedNum;

    private List<Long> seatNumList;

    public InsertTicketDTO toInsertDTO(User user){
        return InsertTicketDTO.builder().ticketState(TicketState.N).stdPrice(0).user(user).build();
    }

    public UpdateTicketDTO toUpdateDTO(){
        return UpdateTicketDTO.builder().ticketState(TicketState.C).build();
    }

    public InsertUpdateTicketDTO toInsertUpdateDTO(User user, List<ScheduleSeat> scheduleSeats){
        return InsertUpdateTicketDTO.builder().ticketNum(ticketNum).insertTicketDTO(toInsertDTO(user)).updateTicketDTO(toUpdateDTO()).scheduleSeats(scheduleSeats).build();
    }
}