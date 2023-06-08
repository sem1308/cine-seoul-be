package uos.cineseoul.dto.fix;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class FixEventDTO {

    @NotNull
    private Long eventNum;
    private Long views;
    private String contents;
    private String image;

    private String title;
    private LocalDateTime startAt;

    private LocalDateTime endAt;
    private String banner;

}
