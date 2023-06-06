package uos.cineseoul.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uos.cineseoul.utils.enums.ActorRole;

import javax.persistence.Access;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActorAndRole {
    @NotNull
    private Long actor;
    @NotBlank
    private ActorRole role;
}
