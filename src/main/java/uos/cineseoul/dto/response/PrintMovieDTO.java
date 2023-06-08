package uos.cineseoul.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.utils.enums.Is;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor()
public class PrintMovieDTO {
    private Long movieNum;

    private String title;

    private String info;

    private String releaseDate;

    private Integer runningTime;

    private Integer ticketCount;

    private Is isShowing;

    private String distName;

    private String gradeName;

    private String poster;

    private List<PrintGenreDTO> genreList;


    public PrintMovieDTO(Movie movie) {
        this.movieNum = movie.getMovieNum();
        this.title = movie.getTitle();
        this.releaseDate = movie.getReleaseDate();
        this.runningTime = movie.getRunningTime();
        this.ticketCount = movie.getReservationCount();
        this.isShowing = movie.getIsShowing();
        if(movie.getDistributor()!=null)
            this.distName = movie.getDistributor().getName();
        this.gradeName = movie.getGrade().getName();
        this.info = movie.getInfo();
        this.poster = movie.getPoster();
        this.genreList = new ArrayList<>();
        movie.getMovieGenreList().forEach(
                movieGenre -> genreList.add(new PrintGenreDTO(movieGenre.getGenre()))
        );
    }
}
