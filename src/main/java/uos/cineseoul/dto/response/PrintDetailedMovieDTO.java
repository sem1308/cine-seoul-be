package uos.cineseoul.dto.response;

import lombok.Data;
import uos.cineseoul.entity.movie.Movie;

import java.util.ArrayList;
import java.util.List;

@Data
public class PrintDetailedMovieDTO {

    private Long movieNum;

    private String title;

    private String info;

    private String releaseDate;

    private int runningTime;

    private char isShowing;

    private String distName;

    private String gradeName;

    private List<String> genreList;

    private List<String> directorList;

    private List<String> actorList;

    public PrintDetailedMovieDTO(Movie movie) {
        this.movieNum = movie.getMovieNum();
        this.title = movie.getTitle();
        this.info = movie.getInfo();
        this.releaseDate = movie.getReleaseDate();
        this.runningTime = movie.getRunningTime();
        this.isShowing = movie.getIsShowing();
        this.distName = movie.getDistributor().getName();
        this.gradeName = movie.getGrade().getName();
        this.genreList = new ArrayList<>();
        this.directorList = new ArrayList<>();
        this.actorList = new ArrayList<>();
        movie.getMovieGenreList().forEach(
                movieGenre -> genreList.add(movieGenre.getGenre().getName())
        );
        movie.getMovieActorList().forEach(
                movieActor -> actorList.add(movieActor.getActor().getName())
        );
        movie.getMovieDirectorList().forEach(
                movieDirector -> directorList.add(movieDirector.getDirector().getName())
        );
    }
}
