package uos.cineseoul.dto.create;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateGradeDTO {
    @NotBlank
    @Size(min = 2, max = 2)
    private String gradeCode;

    @NotBlank
    @Size(max = 20)
    private String name;

}
