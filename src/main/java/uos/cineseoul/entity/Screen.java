package uos.cineseoul.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
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

    @Column(name="NAME", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name="TOTAL_SEAT", nullable = true, unique = false, columnDefinition = "INT DEFAULT 0")
    private Integer totalSeat;

    /* Foreign Key */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCREEN_NUM", insertable = false)
    private List<Seat> seats;
    /* */
}
