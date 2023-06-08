package uos.cineseoul.dto.create;

import lombok.Data;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CreateReviewDTO {

    private String Contents;

    @Min(value = 0)
    @Max(value = 10)
    @NotNull
    private Integer score;

}
