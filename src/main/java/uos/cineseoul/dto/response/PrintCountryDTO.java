package uos.cineseoul.dto.response;

import lombok.Data;
import uos.cineseoul.entity.Country;

@Data
public class PrintCountryDTO {
    private String countryCode;
    private String name;

    public PrintCountryDTO(Country country) {
        this.countryCode = country.getCountryCode();
        this.name = country.getName();
    }
}
