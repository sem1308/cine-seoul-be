package uos.cineseoul.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACT_NUM")
    private Long actNum;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "actor")
    private List<MovieActor> movieActorList;
}
