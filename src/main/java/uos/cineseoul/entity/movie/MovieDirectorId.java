package uos.cineseoul.entity.movie;

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
    private Long movie;
    private Long director;
}
