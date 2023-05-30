package uos.cineseoul.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InsertActorDTO {
    private String name;

    public InsertActorDTO(CreateActorDTO createActorDTO) {
        this.name = createActorDTO.getName();
    }
}
