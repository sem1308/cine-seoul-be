package uos.cineseoul.dto.response;

import lombok.*;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintScheduleSeatDTO {
    private PrintScheduleDTO schedule;

    private PrintSeatDTO seat;

    private String occupied;
}
