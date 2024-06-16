package uos.cineseoul.entity.movie;

import lombok.*;
import uos.cineseoul.entity.Review;
import uos.cineseoul.utils.enums.Is;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVIE_NUM")
    private Long movieNum;

    @Column(name = "TITLE", length = 100, nullable = false)
    private String title;

    @Column(name = "INFO", length = 4000, nullable = true)
    private String info;

    @Column(name = "RELEASE_DATE", columnDefinition = "CHAR(8)", nullable = true)
    private String releaseDate;

    @Column(name = "RUNNING_TIME", nullable = true)
    private Integer runningTime;

    @Column(name = "POSTER", nullable = true)
    private String poster;

    @Column(name = "REVERVATION_COUNT", nullable = false, columnDefinition = "INT DEFAULT 0")
    @Builder.Default
    private Integer reservationCount = 0;

    @Column(name = "IS_SHOWING", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    @Enumerated(EnumType.STRING)
    private Is isShowing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIST_NUM", nullable = true)
    private Distributor distributor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRADE_CODE", nullable = true)
    private Grade grade;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MovieGenre> movieGenreList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MovieDirector> movieDirectorList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MovieActor> movieActorList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MovieCountry> movieCountryList = new ArrayList<>();

    @Builder
    public Movie(Long movieNum, String title, String info, String releaseDate, int runningTime, String poster, Integer reservationCount, Is isShowing, Distributor distributor, Grade grade, List<MovieGenre> movieGenreList, List<MovieDirector> movieDirectorList, List<MovieActor> movieActorList) {
        this.movieNum = movieNum;
        this.title = title;
        this.info = info;
        this.releaseDate = releaseDate;
        this.runningTime = runningTime;
        this.poster = poster;
        this.reservationCount = reservationCount;;
        this.isShowing = isShowing;
        this.distributor = distributor;
        this.grade = grade;
        this.movieGenreList = movieGenreList;
        this.movieDirectorList = movieDirectorList;
        this.movieActorList = movieActorList;
        this.distributor.getMovieList().add(this);
        this.grade.getMovieList().add(this);
    }
}
