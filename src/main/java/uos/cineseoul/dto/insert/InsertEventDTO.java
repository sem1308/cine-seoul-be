package uos.cineseoul.dto.insert;

import lombok.Data;
import uos.cineseoul.dto.create.CreateEventDTO;

import java.time.LocalDateTime;

@Data
public class InsertEventDTO {

    private String contents;

    private String title;

    private LocalDateTime startAt;

    private LocalDateTime endAt;
    private String banner;
    private String image;

    public InsertEventDTO(CreateEventDTO createEventDTO) {
        this.contents = createEventDTO.getContents();
        this.title = createEventDTO.getTitle();
        this.startAt = createEventDTO.getStartAt();
        this.endAt = createEventDTO.getEndAt();
        this.banner = createEventDTO.getBanner();
        this.image = createEventDTO.getImage();
    }
}
