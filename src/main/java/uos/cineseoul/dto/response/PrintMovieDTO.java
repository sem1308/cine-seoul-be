package uos.cineseoul.dto.response;

import lombok.Data;
import uos.cineseoul.entity.movie.Movie;

import java.util.ArrayList;
import java.util.List;

@Data
public class PrintMovieDTO {
    private Long movieNum;

    private String title;

    private String releaseDate;

    private int runningTime;

    private char isShowing;

    private String distName;

    private String gradeName;

    private List<String> genreList;


    public PrintMovieDTO(Movie movie) {
        this.movieNum = movie.getMovieNum();
        this.title = movie.getTitle();
        this.releaseDate = movie.getReleaseDate();
        this.runningTime = movie.getRunningTime();
        this.isShowing = movie.getIsShowing();
        if(movie.getDistributor()!=null)
            this.distName = movie.getDistributor().getName();
        this.gradeName = movie.getGrade().getName();
        this.genreList = new ArrayList<>();
        movie.getMovieGenreList().forEach(
                movieGenre -> genreList.add(movieGenre.getGenre().getName())
        );
    }
}
