package uos.cineseoul.dto.insert;


import lombok.AllArgsConstructor;
import lombok.Getter;
import uos.cineseoul.dto.create.CreateCountryDTO;

@AllArgsConstructor
@Getter
public class InsertCountryDTO {

    private String countryCode;

    private String name;

    public InsertCountryDTO(CreateCountryDTO createCountryDTO) {
        this.countryCode = createCountryDTO.getCountryCode();
        this.name = createCountryDTO.getName();
    }
}
