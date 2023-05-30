package uos.cineseoul.entity.movie;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Builder
@Getter
@IdClass(MovieDirectorId.class)
public class MovieDirector {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_NUM")
    private Movie movie;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIR_NUM")
    private Director director;

    @Builder
    public MovieDirector(Movie movie, Director director) {
        this.movie = movie;
        this.director = director;
        movie.getMovieDirectorList().add(this);
        director.getMovieDirectorList().add(this);
    }
}
