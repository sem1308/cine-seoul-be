package uos.cineseoul.dto;

import lombok.Data;
import uos.cineseoul.entity.Director;

@Data
public class PrintDirectorDTO {
    private Long dirNum;
    private String name;

    public PrintDirectorDTO(Director director) {
        this.dirNum = director.getDirNum();
        this.name = director.getName();
    }
}
