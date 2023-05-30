package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.MovieActor;
import uos.cineseoul.entity.MovieActorId;

public interface MovieActorRepository extends JpaRepository<MovieActor, MovieActorId> {
}
