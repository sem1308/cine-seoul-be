package uos.cineseoul.dto.insert;

import lombok.Data;
import uos.cineseoul.dto.create.CreateReviewDTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class InsertReviewDTO {

    private Long movieNum;
    private String Contents;

    private Integer score;


    public InsertReviewDTO(CreateReviewDTO createReviewDTO) {
        this.movieNum = createReviewDTO.getMovieNum();
        Contents = createReviewDTO.getContents();
        this.score = createReviewDTO.getScore();
    }
}
