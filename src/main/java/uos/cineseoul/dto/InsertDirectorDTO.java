package uos.cineseoul.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InsertDirectorDTO {

    private String name;

    public InsertDirectorDTO(CreateDirectorDTO createDirectorDTO) {
        this.name = createDirectorDTO.getName();
    }
}
