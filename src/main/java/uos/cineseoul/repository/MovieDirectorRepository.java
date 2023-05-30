package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.movie.MovieDirector;
import uos.cineseoul.entity.movie.MovieDirectorId;

public interface MovieDirectorRepository extends JpaRepository<MovieDirector, MovieDirectorId> {
}
