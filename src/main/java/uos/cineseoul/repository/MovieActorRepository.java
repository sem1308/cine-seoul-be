package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.movie.MovieActor;
import uos.cineseoul.entity.movie.MovieActorId;

public interface MovieActorRepository extends JpaRepository<MovieActor, MovieActorId> {
}
