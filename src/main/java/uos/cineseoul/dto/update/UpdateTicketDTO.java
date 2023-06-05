package uos.cineseoul.dto.update;

import lombok.*;
import uos.cineseoul.utils.enums.TicketState;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class UpdateTicketDTO {
    /* 티켓 변경 가능 속성 */
    // 판매 가격
    // 티켓 상태
    private Integer salePrice;

    @Enumerated(EnumType.STRING)
    private TicketState ticketState;
}
