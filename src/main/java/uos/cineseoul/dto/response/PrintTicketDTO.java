package uos.cineseoul.dto.response;

import lombok.*;
import uos.cineseoul.utils.enums.TicketState;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintTicketDTO {
    private Long ticketNum;

    private Integer stdPrice;

    private Integer salePrice;

    @Enumerated(EnumType.STRING)
    private TicketState ticketState;

    private LocalDateTime createdAt;

    private PrintUserDTO user;

    private PrintScheduleSeatForTicketDTO scheduleSeat;
}
