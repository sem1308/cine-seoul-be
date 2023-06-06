package uos.cineseoul.dto.insert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uos.cineseoul.dto.create.CreateMovieDTO;
import uos.cineseoul.entity.movie.Genre;
import uos.cineseoul.utils.ActorAndRole;
import uos.cineseoul.utils.enums.Is;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class InsertMovieDTO {

    private String title;

    private String info;

    private String releaseDate;

    private String poster;

    private int runningTime;

    private Is isShowing;

    private Long distNum;

    private String gradeCode;

    private List<String> genreList;

    private List<ActorAndRole> actorList;

    private List<Long> directorList;

    private List<String> countryList;

    public InsertMovieDTO(CreateMovieDTO createMovieDTO) {
        this.title = createMovieDTO.getTitle();
        this.info = createMovieDTO.getInfo();
        this.releaseDate = createMovieDTO.getReleaseDate();
        this.runningTime = createMovieDTO.getRunningTime();
        this.isShowing = createMovieDTO.getIsShowing();
        this.distNum = createMovieDTO.getDistNum();
        this.poster = createMovieDTO.getPoster();
        this.gradeCode = createMovieDTO.getGradeCode();
        this.genreList = createMovieDTO.getGenreCodeList() != null ? createMovieDTO.getGenreCodeList() : new ArrayList<>();
        this.actorList = createMovieDTO.getActorNumList() != null ? createMovieDTO.getActorNumList() : new ArrayList<>();
        this.directorList = createMovieDTO.getDirectorNumList() != null ? createMovieDTO.getDirectorNumList() : new ArrayList<>();
        this.countryList = createMovieDTO.getCountryCodeList() != null ? createMovieDTO.getCountryCodeList() : new ArrayList<>();
    }
}
