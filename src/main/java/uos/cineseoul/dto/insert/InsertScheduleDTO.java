package uos.cineseoul.dto.insert;

import lombok.*;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.movie.Movie;

import java.time.LocalDateTime;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class InsertScheduleDTO {
    private LocalDateTime schedTime;

    private Integer order;

    private Screen screen;

    private Movie movie;
}
