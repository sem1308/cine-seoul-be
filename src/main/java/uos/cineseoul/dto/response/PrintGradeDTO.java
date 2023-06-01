package uos.cineseoul.dto.response;

import lombok.Data;
import uos.cineseoul.entity.movie.Grade;

@Data
public class PrintGradeDTO {
    private String gradeCode;
    private String name;

    public PrintGradeDTO(Grade grade) {
        this.gradeCode = grade.getGradeCode();
        this.name = grade.getName();
    }
}
