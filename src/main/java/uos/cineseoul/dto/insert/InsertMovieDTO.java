package uos.cineseoul.dto.insert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uos.cineseoul.dto.create.CreateMovieDTO;

import java.util.List;

@AllArgsConstructor
@Getter
public class InsertMovieDTO {

    private String title;

    private String info;

    private String releaseDate;

    private int runningTime;

    private char isShowing;

    private Long distNum;

    private String gradeCode;

    private List<String> genreList;

    private List<Long> actorList;

    private List<Long> directorList;

    public InsertMovieDTO(CreateMovieDTO createMovieDTO) {
        this.title = createMovieDTO.getTitle();
        this.info = createMovieDTO.getInfo();
        this.releaseDate = createMovieDTO.getReleaseDate();
        this.runningTime = createMovieDTO.getRunningTime();
        this.isShowing = createMovieDTO.getIsShowing();
        this.distNum = createMovieDTO.getDistNum();
        this.gradeCode = createMovieDTO.getGradeCode();
        this.genreList = createMovieDTO.getGenreCodeList();
        this.actorList = createMovieDTO.getActorNumList();
        this.directorList = createMovieDTO.getDirectorNumList();
    }
}
