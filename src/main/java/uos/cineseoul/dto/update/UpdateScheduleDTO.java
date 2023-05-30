package uos.cineseoul.dto.update;

import lombok.*;
import uos.cineseoul.entity.Screen;

import java.time.LocalDateTime;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class UpdateScheduleDTO {
    private LocalDateTime schedTime;

    private Integer order;

    private Screen screen;
}
