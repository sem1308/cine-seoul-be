package uos.cineseoul.dto.response;

import lombok.Data;
import uos.cineseoul.entity.Event;

import java.time.LocalDateTime;

@Data
public class PrintEventDTO {

    private Long EventNum;

    private Long views;

    private String userId;

    private String Contents;

    private String IMAGE;

    private LocalDateTime createdAt;

    public PrintEventDTO(Event event) {
        this.EventNum = event.getEventNum();
        this.views = event.getViews();
        this.userId = event.getUser().getId();
        this.Contents = event.getContents();
        this.IMAGE = event.getIMAGE();
        this.createdAt = event.getCreatedAt();
    }
}
