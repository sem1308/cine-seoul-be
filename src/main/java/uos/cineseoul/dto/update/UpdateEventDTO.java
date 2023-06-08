package uos.cineseoul.dto.update;

import lombok.Data;
import uos.cineseoul.dto.fix.FixEventDTO;

@Data
public class UpdateEventDTO {

    private Long eventNum;
    private Long views;

    private String Contents;

    private String IMAGE;

    public UpdateEventDTO(FixEventDTO fixEventDTO) {
        this.eventNum = fixEventDTO.getEventNum();
        this.views = fixEventDTO.getViews();
        Contents = fixEventDTO.getContents();
        this.IMAGE = fixEventDTO.getIMAGE();
    }
}
