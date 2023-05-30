package uos.cineseoul.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintScreenDTO {
    private Long screenNum;

    private String name;

    private Integer totalSeat;

    private List<PrintSeatDTO> seats;
}
