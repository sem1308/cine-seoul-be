package uos.cineseoul.dto.response;

import lombok.Data;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.utils.enums.Is;

import java.util.ArrayList;
import java.util.List;

@Data
public class PrintDetailedMovieDTO {

    private Long movieNum;

    private String title;

    private String info;

    private String releaseDate;

    private int runningTime;

    private String poster;

    private Is isShowing;

    private String distName;

    private PrintGradeDTO gradeName;

    private List<PrintGenreDTO> genreList;

    private List<PrintDirectorDTO> directorList;

    private List<PrintActorDTO> actorList;

    private List<PrintCountryDTO> countryList;

    public PrintDetailedMovieDTO(Movie movie) {
        this.movieNum = movie.getMovieNum();
        this.title = movie.getTitle();
        this.info = movie.getInfo();
        this.releaseDate = movie.getReleaseDate();
        this.runningTime = movie.getRunningTime();
        this.poster = movie.getPoster();
        this.isShowing = movie.getIsShowing();
        if(movie.getDistributor()!=null)
            this.distName = movie.getDistributor().getName();
        this.gradeName = new PrintGradeDTO(movie.getGrade());
        this.genreList = new ArrayList<>();
        this.directorList = new ArrayList<>();
        this.actorList = new ArrayList<>();
        movie.getMovieGenreList().forEach(
                movieGenre -> genreList.add(new PrintGenreDTO(movieGenre.getGenre()))
        );
        movie.getMovieActorList().forEach(
                movieActor -> actorList.add(new PrintActorDTO(movieActor.getActor()))
        );
        movie.getMovieDirectorList().forEach(
                movieDirector -> directorList.add(new PrintDirectorDTO(movieDirector.getDirector()))
        );

        movie.getMovieCountryList().forEach(
                movieCountry -> countryList.add(new PrintCountryDTO(movieCountry.getCountry()))
        );
    }
}
