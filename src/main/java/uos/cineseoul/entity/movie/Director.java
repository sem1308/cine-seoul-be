package uos.cineseoul.entity.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uos.cineseoul.entity.Country;

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

    @Column(name = "IMG_URL", length = 2000, nullable = true)
    private String imgUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "director")
    @Builder.Default
    private List<MovieDirector> movieDirectorList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUNTRY_CODE")
    private Country country;
}
