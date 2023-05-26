package uos.cineseoul.dto;

import lombok.*;
import uos.cineseoul.utils.enums.TicketState;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class UpdateTicketDTO {
    private Long ticketNum ;

    private Integer salePrice;

    @Enumerated(EnumType.STRING)
    private TicketState issued;

    private Long schedNum;

    private Long seatNum;
}
