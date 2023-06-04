package uos.cineseoul.dto.complex;

import lombok.*;
import uos.cineseoul.dto.insert.InsertReservationDTO;
import uos.cineseoul.dto.misc.SeatTypeDTO;
import uos.cineseoul.dto.update.UpdateTicketDTO;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.utils.enums.AudienceType;
import uos.cineseoul.utils.enums.TicketState;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class ReRegisterTicketDTO {
    private Long ticketNum;

    private Long userNum;

    private Long schedNum;

    private List<SeatTypeDTO> seatTypeDTOS;

    public UpdateTicketDTO toUpdateDTO(AtomicReference<Integer> totalPrice){
        return UpdateTicketDTO.builder().stdPrice(totalPrice.get()).salePrice(0).ticketState(TicketState.N).build();
    }
}