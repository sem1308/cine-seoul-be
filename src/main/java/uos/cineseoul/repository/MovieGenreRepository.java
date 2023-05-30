package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.movie.MovieGenre;
import uos.cineseoul.entity.movie.MovieGenreId;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, MovieGenreId> {
}
