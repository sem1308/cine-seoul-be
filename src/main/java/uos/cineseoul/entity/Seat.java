package uos.cineseoul.entity;

import lombok.*;
import uos.cineseoul.utils.enums.GradeType;

import javax.persistence.*;

@Entity(name = "SEAT")
@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SEAT_NUM")
    private Long seatNum;

    @Column(name="S_ROW", nullable = false, unique = false, columnDefinition = "char(1)")
    private String row;

    @Column(name="S_COL", nullable = false, unique = false, length = 2)
    private String col;

    @Column(name="SEAT_GRADE", nullable = false, unique = false, columnDefinition = "char(1)")
    @Enumerated(EnumType.STRING)
    private GradeType seatGrade;

    /* Foreign Key */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCREEN_NUM", nullable = false)
    private Screen screen;
    /* */
}
