package uos.cineseoul.dto.insert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uos.cineseoul.dto.create.CreateActorDTO;

import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
public class InsertActorDTO {
    private String name;

    private String imgUrl;

    public InsertActorDTO(CreateActorDTO createActorDTO) {
        this.name = createActorDTO.getName();
        this.imgUrl = createActorDTO.getImgUrl();
    }
}
