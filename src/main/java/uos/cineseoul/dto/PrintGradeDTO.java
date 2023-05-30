package uos.cineseoul.dto;

import uos.cineseoul.entity.Grade;

public class PrintGradeDTO {
    private String gradeCode;
    private String name;

    public PrintGradeDTO(Grade grade) {
        this.gradeCode = grade.getGradeCode();
        this.name = grade.getName();
    }
}
