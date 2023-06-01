package uos.cineseoul.dto.create;

import lombok.*;
import uos.cineseoul.dto.insert.InsertTicketDTO;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.User;
import uos.cineseoul.utils.enums.TicketState;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class CreateTicketDTO {
    private Integer stdPrice;

    private Integer salePrice;

    @Enumerated(EnumType.STRING)
    private TicketState ticketState;

    private Long userNum;

    private Long schedNum;

    private Long seatNum;

    public InsertTicketDTO toInsertDTO(User user, ScheduleSeat scheduleSeat){
        InsertTicketDTO insertDTO = InsertTicketDTO.builder().stdPrice(stdPrice).salePrice(salePrice)
                .ticketState(TicketState.N).user(user).scheduleSeat(scheduleSeat).build();

        return insertDTO;
    }
}
