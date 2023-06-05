package uos.cineseoul.entity;

import lombok.*;
import uos.cineseoul.utils.enums.AudienceType;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TicketAudienceId {
    private Long ticket;

    private AudienceType audienceType;
}
