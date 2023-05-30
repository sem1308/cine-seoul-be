package uos.cineseoul.dto.create;

import lombok.*;
import uos.cineseoul.dto.insert.InsertScheduleDTO;
import uos.cineseoul.dto.insert.InsertSeatDTO;
import uos.cineseoul.entity.Screen;

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

    public InsertScheduleDTO toInsertDTO(Screen screen){
        InsertScheduleDTO insertDTO = InsertScheduleDTO.builder().schedTime(schedTime).order(order).screen(screen).build();

        return insertDTO;
    }
}
