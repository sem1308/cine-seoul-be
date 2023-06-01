package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.entity.movie.MovieGenre;
import uos.cineseoul.entity.movie.MovieGenreId;

import java.util.List;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, MovieGenreId> {
    List<MovieGenre> findAllByMovie(Movie movie);
}
