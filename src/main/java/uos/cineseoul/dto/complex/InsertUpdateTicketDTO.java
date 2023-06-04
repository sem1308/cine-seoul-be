package uos.cineseoul.dto.complex;

import lombok.*;
import uos.cineseoul.dto.insert.InsertTicketDTO;
import uos.cineseoul.dto.update.UpdateTicketDTO;
import uos.cineseoul.utils.enums.TicketState;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class InsertUpdateTicketDTO {
    private Long ticketNum;
    /* 티켓 변경 가능 속성 */
    // 판매 가격
    // 티켓 상태
    private InsertTicketDTO insertTicketDTO;

    private UpdateTicketDTO updateTicketDTO;
}
