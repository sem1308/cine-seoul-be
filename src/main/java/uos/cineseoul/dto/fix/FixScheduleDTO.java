package uos.cineseoul.dto.fix;

import lombok.*;
import uos.cineseoul.dto.update.UpdateScheduleDTO;
import uos.cineseoul.entity.Screen;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class FixScheduleDTO {
    @NotNull
    private Long schedNum;

    private LocalDateTime schedTime;

    private Integer order;

    private Long screenNum;
}
