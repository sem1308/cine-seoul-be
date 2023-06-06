package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.movie.MovieCountry;
import uos.cineseoul.entity.movie.MovieCountryId;

public interface MovieCountryRepository extends JpaRepository<MovieCountry, MovieCountryId> {
}
