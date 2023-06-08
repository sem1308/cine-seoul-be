package uos.cineseoul.dto.fix;

import lombok.*;
import uos.cineseoul.dto.update.UpdateSeatDTO;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.utils.enums.SeatGrade;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class FixSeatDTO {
    @NotNull
    private Long seatNum;

    // row, col, scrennNum은 전부 null이거나 전부 not null 이어야함
    @Size(max = 1, min = 1)
    private String row;

    @Size(max = 2, min = 1)
    private String col;

    @Size(max = 1, min = 1)
    @Enumerated(EnumType.STRING)
    private SeatGrade seatGrade;

    private Long screenNum;
}
