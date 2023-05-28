package uos.cineseoul.entity;

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
public class Grade {
    @Id
    @Column(name = "GRADE_CODE", columnDefinition = "CHAR(2)", nullable = false, unique = true)
    private String gradeCode;

    @Column(name = "NAME", length = 20, nullable = false)
    private String name;

    @Column(name = "ADULT_ONLY", nullable = false)
    private char adultOnly;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "grade")
    @Builder.Default
    private List<Movie> movieList = new ArrayList<>();
}
