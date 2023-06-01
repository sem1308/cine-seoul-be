package uos.cineseoul.entity;

import lombok.*;
import uos.cineseoul.entity.movie.Movie;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "SCHEDULE")
@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SCHED_NUM")
    private Long schedNum;

    @Column(name="SCHED_TIME", nullable = false)
    private LocalDateTime schedTime;

    @Column(name="SCHED_ORDER", nullable = false, unique = false)
    private Integer order;

    @Column(name="EMPTY_SEAT", nullable = false, unique = false)
    private Integer emptySeat;

    /* Foreign Key */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHED_NUM", nullable = false, insertable = false, updatable = false)
    private List<ScheduleSeat> scheduleSeats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCREEN_NUM", nullable = false)
    private Screen screen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_NUM")
    private Movie movie;
    /* */
}
