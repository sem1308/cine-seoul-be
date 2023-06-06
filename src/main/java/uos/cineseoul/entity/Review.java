package uos.cineseoul.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.lang.reflect.Member;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue
    @Column(name = "REVIEW_NUM")
    private Long reviewNum;

    @Column(name = "CONTENTS", nullable = true, unique = false, length = 1500)
    private String Contents;

    @Column(name = "SCORE", nullable = false)
    @Min(value = 0)
    @Max(value = 10)
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
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
