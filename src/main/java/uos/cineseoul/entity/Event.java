package uos.cineseoul.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
    private Long EventNum;

    @Column(name = "VIEWS")
    @Builder.Default
    private Long views = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NUM")
    private User user;

    @Column(name = "CONTENTS")
    private String Contents;

    @Column(name = "IMAGE")
    private String IMAGE;

    @CreationTimestamp
    @Column(name="CREATED_DATE", nullable = false)
    private LocalDateTime createdAt;

    public void increaseViews() {
        this.views += 1;
    }
}
