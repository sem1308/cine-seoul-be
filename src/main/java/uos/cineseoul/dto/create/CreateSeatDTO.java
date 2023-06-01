package uos.cineseoul.dto.create;

import lombok.*;
import uos.cineseoul.dto.insert.InsertSeatDTO;
import uos.cineseoul.dto.insert.InsertTicketDTO;
import uos.cineseoul.entity.Schedule;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.entity.Seat;
import uos.cineseoul.entity.User;
import uos.cineseoul.utils.enums.GradeType;
import uos.cineseoul.utils.enums.TicketState;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class CreateSeatDTO {
    @Size(max = 1, min = 1)
    private String row;

    @Size(max = 2, min = 1)
    private String col;

    @Size(max = 1, min = 1)
    @Enumerated(EnumType.STRING)
    private GradeType seatGrade;

    private Long screenNum;

    public InsertSeatDTO toInsertDTO(Screen screen){
        InsertSeatDTO insertDTO = InsertSeatDTO.builder().row(row).col(col).seatGrade(seatGrade).screen(screen).build();

        return insertDTO;
    }
}