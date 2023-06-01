package uos.cineseoul.dto.insert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uos.cineseoul.dto.create.CreateDirectorDTO;

@AllArgsConstructor
@Getter
public class InsertDirectorDTO {

    private String name;

    private String imgUrl;
    public InsertDirectorDTO(CreateDirectorDTO createDirectorDTO) {
        this.name = createDirectorDTO.getName();
        this.imgUrl = createDirectorDTO.getImgUrl();
    }
}
