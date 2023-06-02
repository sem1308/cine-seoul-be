package uos.cineseoul.dto.response;

import lombok.*;
import uos.cineseoul.utils.enums.Is;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintScheduleSeatForTicketDTO {
    private PrintScheduleNotSchedSeatDTO schedule;

    private PrintSeatDTO seat;

    @Enumerated(EnumType.STRING)
    private Is isOccupied;
}
