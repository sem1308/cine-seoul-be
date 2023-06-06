package uos.cineseoul.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uos.cineseoul.entity.movie.Actor;
import uos.cineseoul.entity.movie.Director;
import uos.cineseoul.entity.movie.MovieCountry;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Country {

    @Id
    @Column(name = "COUNTRY_CODE", columnDefinition = "CHAR(2)", nullable = false, unique = true)
    private String countryCode;

    @Column(name = "NAME", length = 20, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    @Builder.Default
    private List<MovieCountry> movieCountryList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    @Builder.Default
    private List<Actor> actorList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    @Builder.Default
    private List<Director> directorList = new ArrayList<>();
}
