package uos.cineseoul.dto;

import lombok.*;

import javax.validation.constraints.Size;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class UpdateSeatDTO {
    private Long seatNum;

    @Size(max = 1, min = 1)
    private String row;

    @Size(max = 2, min = 1)
    private String col;

    @Size(max = 1, min = 1)
    private String seatGrade;

    private Long screenNum;
}
