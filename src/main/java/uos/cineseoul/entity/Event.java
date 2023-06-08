package uos.cineseoul.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_NUM")
    private Long eventNum;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "VIEWS")
    @Builder.Default
    private Long views = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NUM")
    private User user;

    @Column(name = "CONTENTS")
    @Nullable
    private String contents;

    @Column(name = "IMAGE")
    private String image;

    @Column(name = "BANNER")
    private String banner;

    @Column(name="START_DATE", nullable = false)
    private LocalDateTime startAt;

    @Column(name="END_DATE", nullable = false)
    private LocalDateTime endAt;

    @CreationTimestamp
    @Column(name="CREATED_DATE", nullable = false)
    private LocalDateTime createdAt;

    public void increaseViews() {
        this.views += 1;
    }
}
