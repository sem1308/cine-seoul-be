package uos.cineseoul.dto;

import com.sun.istack.NotNull;
import lombok.*;
import uos.cineseoul.entity.Screen;

import javax.persistence.*;
import javax.validation.constraints.Size;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class InsertSeatDTO {
    @Size(max = 1, min = 1)
    private String row;

    @Size(max = 2, min = 1)
    private String col;

    @Size(max = 1, min = 1)
    private String seatGrade;

    private Long screenNum;
}
