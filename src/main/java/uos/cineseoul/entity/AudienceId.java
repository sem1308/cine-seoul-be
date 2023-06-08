package uos.cineseoul.entity;

import lombok.*;
import uos.cineseoul.utils.enums.AudienceType;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AudienceId implements Serializable {
    private Long ticket;

    private AudienceType audienceType;
}
