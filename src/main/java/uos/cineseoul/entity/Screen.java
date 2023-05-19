package uos.cineseoul.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "SCREEN")
@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class Screen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SCREEN_NUM")
    private Long screenNum;

    @Column(name="NAME", nullable = false, unique = false, length = 100)
    private String name;

    @Column(name="TOTAL_SEAT", nullable = false, unique = false)
    private Integer totalSeat;

    /* Foreign Key */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCREEN_NUM", nullable = false, insertable = false, updatable = false)
    private List<Seat> seats;
    /* */
}
