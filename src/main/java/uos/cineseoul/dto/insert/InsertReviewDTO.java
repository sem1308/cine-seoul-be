package uos.cineseoul.dto.insert;

import lombok.Data;
import uos.cineseoul.dto.create.CreateReviewDTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class InsertReviewDTO {

    private String Contents;

    private Integer score;

    private String userId;

    public InsertReviewDTO(CreateReviewDTO createReviewDTO) {
        Contents = createReviewDTO.getContents();
        this.score = createReviewDTO.getScore();
        this.userId = createReviewDTO.getUserId();
    }
}
