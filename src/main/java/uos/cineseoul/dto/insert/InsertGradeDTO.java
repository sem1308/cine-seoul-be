package uos.cineseoul.dto.insert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uos.cineseoul.dto.create.CreateGradeDTO;

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
