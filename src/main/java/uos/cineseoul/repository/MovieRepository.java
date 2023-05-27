package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByMovieNum(Long movieNum);
    Optional<Movie> findByTitle(String title);
    List<Movie> findAllByIsShowing(char isShowing);

    List<Movie> findAllByReleaseDateAfter(String date);
}

