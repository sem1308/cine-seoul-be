package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.entity.movie.MovieDirector;
import uos.cineseoul.entity.movie.MovieDirectorId;

import java.util.List;

public interface MovieDirectorRepository extends JpaRepository<MovieDirector, MovieDirectorId> {
    List<MovieDirector> findAllByMovie(Movie movie);
}
