package uos.cineseoul.dto;

import lombok.*;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.Screen;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class InsertScheduleDTO {
    private LocalDateTime schedTime;

    private Integer order;

    private Long screenNum;
}
