package uos.cineseoul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertReviewDTO;
import uos.cineseoul.entity.Review;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.MovieRepository;
import uos.cineseoul.repository.ReviewRepository;
import uos.cineseoul.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    private final MovieRepository movieRepository;

    public Page<Review> findAllReviewByMovie(Long movieNum, Pageable pageable) {
        Movie movie = movieRepository.findByMovieNum(movieNum).orElseThrow(
                () -> new ResourceNotFoundException("해당 영화를 찾을 수 없습니다.")
        );
        return reviewRepository.findAllByMovie(movie, pageable);
    }

    public Review insert(Long userNum, InsertReviewDTO insertReviewDTO) {
        Movie movie = movieRepository.findByMovieNum(insertReviewDTO.getMovieNum()).orElseThrow(
                () -> new ResourceNotFoundException("해당 영화를 찾을 수 없습니다.")
        );

        Review review = Review
                .builder()
                .movie(movie)
                .score(insertReviewDTO.getScore())
                .contents(insertReviewDTO.getContents())
                .recommend(0)
                .user(userRepository.findById(userNum)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("해당 유저를 찾을 수 없습니다.")
                        ))
                .build();
        return reviewRepository.save(review);
    }

    public void recommend(Long reviewNum) {
        reviewRepository.findById(reviewNum).orElseThrow(
                () -> new ResourceNotFoundException("해당 번호의 리뷰가 없습니다.")
        ).addRecommend();
    }
}
