package uos.cineseoul.dto.create;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class CreateEventDTO {

    private String contents;

    private String title;

    private LocalDateTime startAt;

    private LocalDateTime endAt;
    private String banner;
    private String image;
}
