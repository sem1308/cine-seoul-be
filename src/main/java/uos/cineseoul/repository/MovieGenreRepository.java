package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.MovieGenre;
import uos.cineseoul.entity.MovieGenreId;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, MovieGenreId> {
}
