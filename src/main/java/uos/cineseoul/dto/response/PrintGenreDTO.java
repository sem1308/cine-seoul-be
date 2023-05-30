package uos.cineseoul.dto.response;

import lombok.Data;
import uos.cineseoul.entity.movie.Genre;

@Data
public class PrintGenreDTO {
    private String genreCode;
    private String name;

    public PrintGenreDTO(Genre genre) {
        this.genreCode = genre.getGenreCode();
        this.name = genre.getName();
    }
}
