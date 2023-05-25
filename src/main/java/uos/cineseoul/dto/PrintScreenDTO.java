package uos.cineseoul.dto;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import uos.cineseoul.entity.Seat;

import javax.persistence.*;
import javax.validation.constraints.Size;
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
