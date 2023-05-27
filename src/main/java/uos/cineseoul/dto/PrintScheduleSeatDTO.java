package uos.cineseoul.dto;

import lombok.*;
import uos.cineseoul.entity.Schedule;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintScheduleSeatDTO {
    private PrintScheduleDTO schedule;

    private PrintSeatDTO seat;

    private String occupied;
}
