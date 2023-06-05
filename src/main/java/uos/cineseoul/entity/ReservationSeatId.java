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
public class ReservationSeatId implements Serializable {
    private Long ticket;

    private Long seat;
}