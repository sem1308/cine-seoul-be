package uos.cineseoul.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import uos.cineseoul.utils.enums.TicketState;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name="SALE_PRICE", nullable = true, unique = false)
    @Builder.Default
    private Integer salePrice = 0;

    @Column(name="TICKET_STATE", nullable = false, unique = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    @Enumerated(EnumType.STRING)
    private TicketState ticketState;

    @CreationTimestamp
    @Column(name="REVERVATION_DATE", nullable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name="CANCEL_DATE", nullable = false)
    private LocalDateTime canceledAt;

    /* Foreign Key */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHED_NUM", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NUM", nullable = false)
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_NUM", nullable = false, insertable = false, updatable = false)
    @Builder.Default
    private List<TicketSeat> ticketSeats = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_NUM", nullable = false, insertable = false, updatable = false)
    @Builder.Default
    private List<Audience> audienceTypes = new ArrayList<>();
}