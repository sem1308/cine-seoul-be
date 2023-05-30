package uos.cineseoul.dto.create;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateDistributorDTO {
    @NotBlank
    @Size(max = 100)
    private String name;
}
