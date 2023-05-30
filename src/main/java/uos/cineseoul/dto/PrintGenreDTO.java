package uos.cineseoul.dto;

import lombok.Data;
import uos.cineseoul.entity.Genre;

@Data
public class PrintGenreDTO {
    private String genreCode;
    private String name;

    public PrintGenreDTO(Genre genre) {
        this.genreCode = genre.getGenreCode();
        this.name = genre.getName();
    }
}
