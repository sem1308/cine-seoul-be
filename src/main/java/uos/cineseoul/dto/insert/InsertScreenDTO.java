package uos.cineseoul.dto.insert;

import lombok.*;

import javax.validation.constraints.Size;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class InsertScreenDTO {
    @Size(max = 100, min = 1)
    private String name;

    private Integer totalSeat;
}
