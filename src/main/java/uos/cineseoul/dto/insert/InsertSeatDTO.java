package uos.cineseoul.dto.insert;

import lombok.*;
import uos.cineseoul.dto.create.CreatePaymentDTO;
import uos.cineseoul.dto.create.CreateSeatDTO;
import uos.cineseoul.entity.Screen;
import uos.cineseoul.utils.enums.SeatGrade;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class InsertSeatDTO {
    private String row;

    private String col;

    @Enumerated(EnumType.STRING)
    private SeatGrade seatGrade;

    private Screen screen;

    public InsertSeatDTO(CreateSeatDTO createSeatDTO){
        this.row = createSeatDTO.getRow();
        this.col = createSeatDTO.getCol();
        this.seatGrade = createSeatDTO.getSeatGrade();
    }

    public InsertSeatDTO(CreateSeatDTO createSeatDTO, Screen screen){
        this.row = createSeatDTO.getRow();
        this.col = createSeatDTO.getCol();
        this.seatGrade = createSeatDTO.getSeatGrade();
        this.screen = screen;
    }
}
