package uos.cineseoul.dto.insert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uos.cineseoul.dto.create.CreateGenreDTO;

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
