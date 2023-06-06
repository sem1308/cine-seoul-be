package uos.cineseoul.dto.complex;

import lombok.*;
import uos.cineseoul.dto.create.CreateTicketAudienceDTO;
import uos.cineseoul.dto.misc.SeatTypeDTO;
import uos.cineseoul.dto.update.UpdateTicketDTO;
import uos.cineseoul.utils.enums.AudienceType;
import uos.cineseoul.utils.enums.TicketState;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class CancelRegisterTicketDTO {
    private Long ticketNum;

    private Long userNum;

    private Long schedNum;

    private Integer stdPrice;

    private List<Long> seatNumList;

    private List<CreateTicketAudienceDTO> createTicketAudienceDTOList;
}