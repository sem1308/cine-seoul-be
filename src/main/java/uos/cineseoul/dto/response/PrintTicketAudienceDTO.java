package uos.cineseoul.dto.response;

import lombok.*;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.utils.enums.AudienceType;
import uos.cineseoul.utils.enums.TicketState;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintTicketAudienceDTO {
    private AudienceType audienceType;

    private String displayName;

    private Integer count;
}
