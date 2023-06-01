package uos.cineseoul.dto.insert;

import lombok.*;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.utils.enums.GradeType;

import javax.persistence.*;
import javax.validation.constraints.Size;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class InsertSeatDTO {
    private String row;

    private String col;

    @Enumerated(EnumType.STRING)
    private GradeType seatGrade;

    private Screen screen;
}
