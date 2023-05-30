package uos.cineseoul.dto.insert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uos.cineseoul.dto.create.CreateActorDTO;

@AllArgsConstructor
@Getter
public class InsertActorDTO {
    private String name;

    public InsertActorDTO(CreateActorDTO createActorDTO) {
        this.name = createActorDTO.getName();
    }
}
