package uos.cineseoul.dto.fix;

import lombok.*;
import uos.cineseoul.dto.update.UpdateTicketDTO;
import uos.cineseoul.utils.enums.TicketState;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class FixTicketDTO {
    /* 티켓 변경 가능 속성 */
    // 판매 가격
    // 티켓 상태

    @NotNull
    private Long ticketNum ;

    private Integer salePrice;

    @Enumerated(EnumType.STRING)
    private TicketState ticketState;
}
