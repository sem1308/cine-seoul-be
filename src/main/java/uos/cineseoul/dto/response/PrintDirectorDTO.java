package uos.cineseoul.dto.response;

import lombok.Data;
import uos.cineseoul.entity.movie.Director;

@Data
public class PrintDirectorDTO {
    private Long dirNum;
    private String name;
    private String imgUrl;

    public PrintDirectorDTO(Director director) {
        this.dirNum = director.getDirNum();
        this.name = director.getName();
        this.imgUrl = director.getImgUrl();
    }
}
