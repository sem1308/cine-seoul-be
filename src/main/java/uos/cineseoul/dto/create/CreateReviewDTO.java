package uos.cineseoul.dto.create;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import uos.cineseoul.entity.User;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CreateReviewDTO {

    private String Contents;

    @Min(value = 0)
    @Max(value = 10)
    @NotNull
    private Integer score;

    //todo 인증과정 확인 필요
    @NotNull
    private String userId;

}
