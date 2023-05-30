package uos.cineseoul.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateActorDTO {
    @NotBlank
    @Size(max = 100)
    private String name;
}
