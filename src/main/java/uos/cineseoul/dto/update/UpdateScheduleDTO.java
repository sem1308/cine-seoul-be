package uos.cineseoul.dto.update;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class UpdateScheduleDTO {
    private Long schedNum;

    private LocalDateTime schedTime;

    private Integer order;

    private Long screenNum;
}
