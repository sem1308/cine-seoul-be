package uos.cineseoul.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class InsertDistributorDTO {

    private String name;

    public InsertDistributorDTO(CreateDistributorDTO createDistributorDTO) {
        this.name = createDistributorDTO.getName();
    }
}
