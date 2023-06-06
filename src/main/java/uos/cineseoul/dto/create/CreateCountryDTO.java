package uos.cineseoul.dto.create;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateCountryDTO {
    @Size(min = 2, max = 2)
    @NotBlank
    private String countryCode;

    @NotBlank
    private String name;
}
