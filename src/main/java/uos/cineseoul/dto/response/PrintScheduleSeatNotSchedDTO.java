package uos.cineseoul.dto.response;

import lombok.*;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintScheduleSeatNotSchedDTO {
    private PrintSeatDTO seat;

    private String occupied;
}
