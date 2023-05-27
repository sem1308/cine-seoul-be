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
public class Genre {
    @Id
    @Column(name = "GENRE_CODE", columnDefinition = "CHAR(2)", nullable = false, unique = true)
    private String genreCode;

    @Column(name = "NAME", length = 20, nullable = false)
    private String name;

    @OneToMany(mappedBy = "genre")
    private List<MovieGenre> movieGenreList;
}
