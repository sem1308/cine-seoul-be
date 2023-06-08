package uos.cineseoul.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.IncreaseReviewRecommend;
import uos.cineseoul.dto.create.CreateReviewDTO;
import uos.cineseoul.dto.insert.InsertReviewDTO;
import uos.cineseoul.dto.response.PrintMovieDTO;
import uos.cineseoul.dto.response.PrintPageDTO;
import uos.cineseoul.dto.response.PrintReviewDTO;
import uos.cineseoul.entity.Review;
import uos.cineseoul.repository.UserRepository;
import uos.cineseoul.service.ReviewService;
import uos.cineseoul.utils.JwtTokenProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    @Operation(description = "게시판 글을 조회한다.")
    public ResponseEntity<PrintPageDTO<PrintReviewDTO>> findAllReview(@RequestParam(name = "page") int page,
                                                                       @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<PrintReviewDTO> printReviewDTOS = new ArrayList<>();
        Page<Review> reviewPage = reviewService.findAllReview(pageable);
        reviewPage.getContent().forEach(
                review -> printReviewDTOS.add(
                        PrintReviewDTO
                                .builder()
                                .reviewNum(review.getReviewNum())
                                .score(review.getScore())
                                .Contents(review.getContents())
                                .createdAt(review.getCreatedAt())
                                .recommend(review.getRecommend())
                                .userId(review.getUser().getId())
                                .build()
                )
        );
        return ResponseEntity.ok(new PrintPageDTO<>(printReviewDTOS, reviewPage.getTotalPages()));
    }

    @GetMapping("/recommend")
    @Operation(description = "게시판 글을 추천순으로 조회한다.")
    public ResponseEntity<PrintPageDTO<PrintReviewDTO>> findAllRecommendReview(@RequestParam(name = "page") int page,
                                                                                @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<PrintReviewDTO> printReviewDTOS = new ArrayList<>();
        Page<Review> reviewPage = reviewService.findAllReview(pageable);
        reviewPage.getContent().forEach(
                review -> printReviewDTOS.add(
                        PrintReviewDTO
                                .builder()
                                .reviewNum(review.getReviewNum())
                                .score(review.getScore())
                                .Contents(review.getContents())
                                .createdAt(review.getCreatedAt())
                                .recommend(review.getRecommend())
                                .userId(review.getUser().getId())
                                .build()
                )
        );
        Collections.sort(printReviewDTOS, Comparator.comparingInt(value -> value.getRecommend()));
        Collections.reverse(printReviewDTOS);
        return ResponseEntity.ok(new PrintPageDTO<>(printReviewDTOS, reviewPage.getTotalPages()));
    }

    @PostMapping
    @Operation(description = "게시판 글을 작성한다.")
    public ResponseEntity<Long> register(@RequestHeader(value = "Authorization") String token,@RequestBody CreateReviewDTO createReviewDTO)
    {
        Long userNum = jwtTokenProvider.getClaims(token).get("num", Long.class);
        Review review = reviewService.insert(userNum, new InsertReviewDTO(createReviewDTO));
        return ResponseEntity.ok(review.getReviewNum());
    }
    @PostMapping("/recommend")
    @Operation(description = "추천수를 증가시킨다.")
    public void recommend(@RequestBody IncreaseReviewRecommend increaseReviewRecommend)
    {
        reviewService.recommend(increaseReviewRecommend.getReviewNum());
    }

}
