package uos.cineseoul.dto.insert;

import lombok.*;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.TicketScheduleSeat;
import uos.cineseoul.entity.User;
import uos.cineseoul.utils.enums.TicketState;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class InsertTicketDTO {
    private Integer stdPrice;

    private Integer salePrice;

    @Enumerated(EnumType.STRING)
    private TicketState ticketState;

    private User user;
}
