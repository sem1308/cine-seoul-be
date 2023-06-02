package uos.cineseoul.dto.update;

import lombok.*;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.utils.enums.SeatGrade;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class UpdateSeatDTO {
    @Size(max = 1, min = 1)
    private String row;

    @Size(max = 2, min = 1)
    private String col;

    @Size(max = 1, min = 1)
    @Enumerated(EnumType.STRING)
    private SeatGrade seatGrade;

    private Screen screen;
}
