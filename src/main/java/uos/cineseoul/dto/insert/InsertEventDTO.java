package uos.cineseoul.dto.insert;

import lombok.Data;
import uos.cineseoul.dto.create.CreateEventDTO;

@Data
public class InsertEventDTO {

    private String Contents;

    private String IMAGE;

    public InsertEventDTO(CreateEventDTO createEventDTO) {
        this.Contents = createEventDTO.getContents();
        this.IMAGE = createEventDTO.getIMAGE();
    }
}
