package uos.cineseoul.entity;

import lombok.*;
import uos.cineseoul.utils.enums.AudienceType;

import javax.persistence.*;

@Entity(name = "AUDIENCE")
@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
@IdClass(AudienceId.class)
public class Audience {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_NUM", nullable = false)
    private Ticket ticket;

    @Id
    @Column(name = "AUDIENCE_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private AudienceType audienceType;

    @Column(name = "COUNT", nullable = false)
    private Integer count;
}
