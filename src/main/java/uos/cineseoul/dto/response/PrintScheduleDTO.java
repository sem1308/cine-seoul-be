package uos.cineseoul.dto.response;

import lombok.*;

import java.time.LocalDateTime;

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
