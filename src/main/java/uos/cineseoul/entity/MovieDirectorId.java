package uos.cineseoul.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class MovieDirectorId implements Serializable {
    private Movie movie;
    private Director director;
}
