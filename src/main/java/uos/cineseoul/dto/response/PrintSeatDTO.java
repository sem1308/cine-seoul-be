package uos.cineseoul.dto.response;

import lombok.*;
import uos.cineseoul.utils.enums.SeatGrade;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintSeatDTO {
    private Long seatNum;

    private String row;

    private String col;

    @Enumerated(EnumType.STRING)
    private SeatGrade seatGrade;

    private Integer seatPrice;

    private Long screenNum;
}
