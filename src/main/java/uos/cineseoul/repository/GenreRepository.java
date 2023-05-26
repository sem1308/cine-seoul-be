package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.Genre;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, String> {
    Optional<Genre> findByGenreCode(String genreCode);
}
