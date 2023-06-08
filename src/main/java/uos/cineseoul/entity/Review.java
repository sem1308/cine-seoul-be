package uos.cineseoul.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import uos.cineseoul.entity.movie.Movie;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "REVIEW")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_NUM")
    private Long reviewNum;

    @Column(name = "CONTENTS", nullable = true, unique = false, length = 300)
    private String contents;

    @Column(name = "SCORE", nullable = false)
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NUM")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MOVIE_NUM")
    private Movie movie;

    @CreationTimestamp
    @Column(name="CREATED_DATE", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "RECOMMEND")
    private Integer recommend;

    public void addRecommend() {
        this.recommend += 1;
    }
}
