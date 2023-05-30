package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.MovieDirector;
import uos.cineseoul.entity.MovieDirectorId;

public interface MovieDirectorRepository extends JpaRepository<MovieDirector, MovieDirectorId> {
}
