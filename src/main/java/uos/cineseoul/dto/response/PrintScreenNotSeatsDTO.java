package uos.cineseoul.dto.response;

import lombok.*;
import uos.cineseoul.entity.Screen;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintScreenNotSeatsDTO {
    private Long screenNum;

    private String name;

    private Integer totalSeat;

    public PrintScreenNotSeatsDTO(Screen screen){
        this.screenNum = screen.getScreenNum();
        this.name = screen.getName();
        this.totalSeat = screen.getTotalSeat();
    }
}
