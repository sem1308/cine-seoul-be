package uos.cineseoul.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertReviewDTO;
import uos.cineseoul.entity.Review;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.ReviewRepository;
import uos.cineseoul.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public Page<Review> findAllReview(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    public Review insert(Long userNum, InsertReviewDTO insertReviewDTO) {
        Review review = Review
                .builder()
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
                () -> new ResourceNotFoundException("해당 번호의 review가 없습니다.")
        ).addRecommend();
    }
}
