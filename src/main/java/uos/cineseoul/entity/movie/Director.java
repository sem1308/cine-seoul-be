package uos.cineseoul.entity.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DIR_NUM")
    private Long dirNum;


    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "director")
    @Builder.Default
    private List<MovieDirector> movieDirectorList = new ArrayList<>();
}
