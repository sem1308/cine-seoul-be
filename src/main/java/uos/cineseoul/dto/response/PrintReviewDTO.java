package uos.cineseoul.dto.response;

import lombok.Builder;
import lombok.Data;
import uos.cineseoul.entity.Review;

import java.time.LocalDateTime;

@Data
@Builder
public class PrintReviewDTO {
    private Long reviewNum;

    private String Contents;

    private Integer score;

    private String userId;

    private LocalDateTime createdAt;

    private Integer recommend;

}
