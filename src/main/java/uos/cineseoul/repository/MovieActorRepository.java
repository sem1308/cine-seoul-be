package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.entity.movie.MovieActor;
import uos.cineseoul.entity.movie.MovieActorId;

import java.util.List;

public interface MovieActorRepository extends JpaRepository<MovieActor, MovieActorId> {
    List<MovieActor> findAllByMovie(Movie movie);
}
