package uos.cineseoul.dto.response;

import lombok.Data;
import uos.cineseoul.entity.Event;

import java.time.LocalDateTime;

@Data
public class PrintEventDTO {

    private Long EventNum;

    private Long views;

    private String userId;

    private String title;

    private String contents;

    private String image;

    private String banner;

    private LocalDateTime createdAt;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    public PrintEventDTO(Event event) {
        EventNum = event.getEventNum();
        this.views = event.getViews();
        this.userId = event.getUser().getId();
        this.title = event.getTitle();
        this.contents = event.getContents();
        this.image = event.getImage();
        this.banner = event.getBanner();
        this.createdAt = event.getCreatedAt();
        this.startAt = event.getStartAt();
        this.endAt = event.getEndAt();
    }
}
