package uos.cineseoul.dto;

import lombok.*;

import javax.validation.constraints.Size;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class UpdateScreenDTO {
    private Long screenNum;

    @Size(max = 100, min = 1)
    private String name;
}
