package uos.cineseoul.dto.fix;

import lombok.Data;
import uos.cineseoul.utils.ActorAndRole;
import uos.cineseoul.utils.enums.Is;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class FixMovieDTO {

    @NotNull
    private Long movie_num;
    @Size(max = 100)
    private String title;

    @Size(max = 4000)
    private String info;

    @Size(min = 8, max = 8)
    private String releaseDate;

    private Integer runningTime;

    private Is isShowing;

    private Long distNum;

    private String poster;

    @Size(min = 2, max = 2)
    private String gradeCode;

    private List<String> genreCodeList;

    private List<Long> directorNumList;

    private List<ActorAndRole> actorNumList;

    private List<String> countryList;
}
