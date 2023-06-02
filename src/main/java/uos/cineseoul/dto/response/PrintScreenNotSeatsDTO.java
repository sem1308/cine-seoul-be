package uos.cineseoul.dto.response;

import lombok.*;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintScreenNotSeatsDTO {
    private Long screenNum;

    private String name;

    private Integer totalSeat;
}
