package uos.cineseoul.entity;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TicketScheduleSeatId implements Serializable {
    private Long ticket;
    private ScheduleSeatId scheduleSeat;
}