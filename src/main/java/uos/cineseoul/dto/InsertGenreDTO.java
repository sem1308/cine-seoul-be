package uos.cineseoul.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class InsertGenreDTO {

    private String genreCode;

    private String name;

    public InsertGenreDTO(CreateGenreDTO createGenreDTO) {
        this.genreCode = createGenreDTO.getGenreCode();
        this.name = createGenreDTO.getName();
    }
}
