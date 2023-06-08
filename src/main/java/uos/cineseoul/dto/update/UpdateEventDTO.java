package uos.cineseoul.dto.update;

import lombok.Data;
import uos.cineseoul.dto.fix.FixEventDTO;

import java.time.LocalDateTime;

@Data
public class UpdateEventDTO {

    private Long eventNum;
    private Long views;
    private String contents;
    private String image;

    private String title;
    private LocalDateTime startAt;

    private LocalDateTime endAt;
    private String banner;

    public UpdateEventDTO(FixEventDTO fixEventDTO) {
        this.eventNum = fixEventDTO.getEventNum();
        this.views = fixEventDTO.getViews();
        this.contents = fixEventDTO.getContents();
        this.image = fixEventDTO.getImage();
        this.title = fixEventDTO.getTitle();
        this.startAt = fixEventDTO.getStartAt();
        this.endAt = fixEventDTO.getEndAt();
        this.banner = fixEventDTO.getBanner();
    }
}
