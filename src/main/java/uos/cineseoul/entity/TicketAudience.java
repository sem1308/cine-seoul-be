package uos.cineseoul.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import uos.cineseoul.utils.enums.AudienceType;
import uos.cineseoul.utils.enums.TicketState;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "TICKET_AUDIENCE")
@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
@IdClass(TicketAudienceId.class)
public class TicketAudience {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "TICKET_NUM", nullable = false)
    private Ticket ticket;

    @Id
    @Column(name = "AUDIENCE_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private AudienceType audienceType;

    @Column(name = "COUNT", nullable = false)
    private Integer count;
}
