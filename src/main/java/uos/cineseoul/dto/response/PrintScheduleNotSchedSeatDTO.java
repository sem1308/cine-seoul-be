package uos.cineseoul.dto.response;

import lombok.*;
import uos.cineseoul.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintScheduleNotSchedSeatDTO {
    private Long schedNum;

    private LocalDateTime schedTime;

    private Integer order;

    private Integer emptySeat;

    private PrintScreenNotSeatsDTO screen;

    private PrintMovieDTO movie;

    public PrintScheduleNotSchedSeatDTO(Schedule schedule){
        this.schedNum = schedule.getSchedNum();
        this.schedTime = schedule.getSchedTime();
        this.order = schedule.getOrder();
        this.emptySeat = schedule.getEmptySeat();
        this.screen = new PrintScreenNotSeatsDTO(schedule.getScreen());
        this.movie = new PrintMovieDTO(schedule.getMovie());
    }
}
