package uos.cineseoul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.Country;
import uos.cineseoul.entity.movie.Genre;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.utils.enums.Is;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findAllByMovieGenreList_Genre(Genre genre);

    List<Movie> findAllByTitleContains(String title);
    Page<Movie> findAllByMovieGenreList_Genre(Genre genre, Pageable pageable);

    Page<Movie> findAllByMovieCountryList_Country(Country country, Pageable pageable);
    Optional<Movie> findByMovieNum(Long movieNum);
    Optional<Movie> findByTitle(String title);
    List<Movie> findAllByIsShowing(Is isShowing);
    Page<Movie> findAllByIsShowing(Is isShowing, Pageable pageable);
    Page<Movie> findAllByIsShowingAndMovieGenreList_Genre(Is isShowing,Genre genre, Pageable pageable);
    List<Movie> findAllByReleaseDateAfter(String date);
    Page<Movie> findAllByReleaseDateAfter(String date, Pageable pageable);
    Page<Movie> findAllByReleaseDateAfterAndMovieGenreList_Genre(String date,Genre genre, Pageable pageable);
}

