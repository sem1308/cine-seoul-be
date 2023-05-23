package uos.cineseoul.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(name="ISSUED", nullable = false, unique = false, length = 1)
    //@ColumnDefault("N")
    private String issued;

    @CreationTimestamp
    @Column(name="CREATED_AT", nullable = false)
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
    /* */
}
