package uos.cineseoul.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    @Builder.Default
    private Integer totalSeat = 0;

    /* Foreign Key */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCREEN_NUM", insertable = false)
    private List<Seat> seats;
    /* */

    public static Screen mock(){
        Random random = new Random();
        int randNum = random.nextInt();
        Screen screen = Screen.builder()
            .name("name" + randNum)
            .seats(new ArrayList<>())
            .build();
        return screen;
    }
}
