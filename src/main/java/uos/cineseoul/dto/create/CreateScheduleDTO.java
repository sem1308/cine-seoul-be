package uos.cineseoul.dto.create;

import lombok.*;
import uos.cineseoul.dto.insert.InsertScheduleDTO;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.movie.Movie;

import java.time.LocalDateTime;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class CreateScheduleDTO {
    private LocalDateTime schedTime;

    private Integer order;

    private Long screenNum;

    private Long movieNum;

    public InsertScheduleDTO toInsertDTO(Screen screen, Movie movie){
        InsertScheduleDTO insertDTO = InsertScheduleDTO.builder().schedTime(schedTime).order(order).movie(movie).screen(screen).build();

        return insertDTO;
    }
}
