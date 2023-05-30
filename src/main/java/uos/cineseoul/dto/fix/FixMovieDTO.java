package uos.cineseoul.dto.fix;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class FixMovieDTO {
    @Size(max = 100)
    private String title;

    @Size(max = 4000)
    private String info;

    @Size(min = 8, max = 8)
    private String releaseDate;

    private int runningTime;

    private char isShowing;

    private Long distNum;

    @Size(min = 2, max = 2)
    private String gradeCode;

    private List<String> genreCodeList;

    private List<Long> directorNumList;

    private List<Long> actorNumList;
}
