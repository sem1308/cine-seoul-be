package uos.cineseoul.dto.fix;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FixEventDTO {

    @NotNull
    private Long eventNum;
    private Long views;
    private String Contents;
    private String IMAGE;
}
