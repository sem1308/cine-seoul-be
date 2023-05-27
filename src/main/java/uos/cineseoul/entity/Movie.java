package uos.cineseoul.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uos.cineseoul.entity.domain.CharLength;
import uos.cineseoul.entity.domain.VarcharLength;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVIE_NUM")
    private Long movieNum;

    @Column(name = "TITLE", length = 100, nullable = false)
    private String title;

    @Column(name = "INFO", length = 4000, nullable = false)
    private String info;

    @Column(name = "RELEASE_DATE", columnDefinition = "CHAR(8)", nullable = true)
    private String releaseDate;

    @Column(name = "RUNNING_TIME", nullable = true)
    private int runningTime;

    @Column(name = "IS_SHOWING", nullable = false)
    private char isShowing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIST_NUM")
    private Distributor distributor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRADE_CODE")
    private Grade grade;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie")
    private List<MovieGenre> movieGenreList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie")
    private List<MovieDirector> movieDirectorList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie")
    private List<MovieActor> movieActorList;

}