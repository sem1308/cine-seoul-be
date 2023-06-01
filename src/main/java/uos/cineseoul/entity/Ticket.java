package uos.cineseoul.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import uos.cineseoul.utils.enums.TicketState;

@Entity(name = "TICKET")
@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class Ticket{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TICKET_NUM")
    private Long ticketNum;

    @Column(name="STD_PRICE", nullable = false, unique = false)
    private Integer stdPrice;

    @Column(name="SALE_PRICE", nullable = false, unique = false)
    private Integer salePrice;

    @Column(name="TICKET_STATE", nullable = false, unique = false, columnDefinition = "char(1)")
    @Enumerated(EnumType.STRING)
    private TicketState ticketState;

    @CreationTimestamp
    @Column(name="REVERVATION_DATE", nullable = false)
    private LocalDateTime createdAt;

    /* Foreign Key */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NUM", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "SCHED_NUM", referencedColumnName = "SCHED_NUM"),
            @JoinColumn(name = "SEAT_NUM", referencedColumnName = "SEAT_NUM")
    })
    private ScheduleSeat scheduleSeat;
}
