package uos.cineseoul.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class InsertGradeDTO {

    private String gradeCode;

    private String name;

    private char adultOnly;

    public InsertGradeDTO(CreateGradeDTO createGradeDTO) {
        this.gradeCode = createGradeDTO.getGradeCode();
        this.name = createGradeDTO.getName();
        // TODO adultOnly 로직
        this.adultOnly = 'F';
    }
}
