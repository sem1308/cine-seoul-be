package uos.cineseoul.dto.insert;

import lombok.Data;
import uos.cineseoul.dto.create.CreateEventDTO;

@Data
public class InsertEventDTO {

    private String userId;

    private String Contents;

    private String IMAGE;

    public InsertEventDTO(CreateEventDTO createEventDTO) {
        this.userId = createEventDTO.getUserId();
        this.Contents = createEventDTO.getContents();
        this.IMAGE = createEventDTO.getIMAGE();
    }
}
