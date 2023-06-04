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

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketScheduleSeat> ticketScheduleSeats = new ArrayList<>();

}
