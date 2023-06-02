package uos.cineseoul.dto.response;

import lombok.*;

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

    private PrintScreenNotSeatsDTO screen;

    private PrintMovieDTO movie;

    private List<PrintScheduleSeatNotSchedDTO> scheduleSeats;
}
