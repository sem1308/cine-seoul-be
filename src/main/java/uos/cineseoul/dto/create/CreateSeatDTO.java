package uos.cineseoul.dto.create;

import lombok.*;
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
public class CreateSeatDTO {
    @NotNull
    @Size(max = 1, min = 1)
    private String row;

    @NotNull
    @Size(max = 2, min = 1)
    private String col;

    @NotNull
    @Size(max = 1, min = 1)
    @Enumerated(EnumType.STRING)
    private SeatGrade seatGrade;

    @NotNull
    private Long screenNum;
}
