package uos.cineseoul.dto.insert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uos.cineseoul.dto.create.CreateDistributorDTO;

@AllArgsConstructor
@Getter
public class InsertDistributorDTO {

    private String name;

    public InsertDistributorDTO(CreateDistributorDTO createDistributorDTO) {
        this.name = createDistributorDTO.getName();
    }
}
