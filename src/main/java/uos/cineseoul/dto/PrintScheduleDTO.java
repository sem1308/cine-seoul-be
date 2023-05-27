package uos.cineseoul.dto;

import lombok.*;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.Screen;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintScheduleDTO {
    private Long schedNum;

    private LocalDateTime schedTime;

    private Integer order;

    private Integer emptySeat;

    private PrintScreenDTO screen;
}
