package uos.cineseoul.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(name = "CONTENTS", nullable = true, unique = false, length = 1500)
    private String contents;

    @Column(name = "SCORE", nullable = false)
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NUM")
    private User user;

    @CreationTimestamp
    @Column(name="CREATED_DATE", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "RECOMMEND")
    private Integer recommend;

    public void addRecommend() {
        this.recommend += 1;
    }
}
