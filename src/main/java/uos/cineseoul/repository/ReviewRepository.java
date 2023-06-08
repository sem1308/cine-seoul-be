package uos.cineseoul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.Review;
import uos.cineseoul.entity.movie.Movie;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByMovie(Movie movie, Pageable pageable);

}
