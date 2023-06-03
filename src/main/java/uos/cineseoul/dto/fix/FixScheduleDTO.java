package uos.cineseoul.dto.fix;

import lombok.*;
import uos.cineseoul.dto.update.UpdateScheduleDTO;
import uos.cineseoul.entity.Screen;

import java.time.LocalDateTime;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class FixScheduleDTO {
    private Long schedNum;

    private LocalDateTime schedTime;

    private Integer order;

    private Long screenNum;

    public UpdateScheduleDTO toUpdateDTO(Screen screen){
        return UpdateScheduleDTO.builder().schedTime(schedTime).order(order).screen(screen).build();
    }
}
