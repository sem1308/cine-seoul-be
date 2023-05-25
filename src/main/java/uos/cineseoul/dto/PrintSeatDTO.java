package uos.cineseoul.dto;

import lombok.*;

import javax.validation.constraints.Size;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintSeatDTO {
    private String row;

    private String col;

    private String seatGrade;

    private Long screenNum;
}
