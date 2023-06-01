package uos.cineseoul.entity.movie;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Builder
@IdClass(MovieActorId.class)
public class MovieActor {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOIVE_NUM")
    private Movie movie;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACT_NUM")
    private Actor actor;

    @Column(name = "IS_MAIN", columnDefinition = "CHAR(1)", nullable = true)
    private String isMain;

    @Builder
    public MovieActor(Movie movie, Actor actor, String isMain) {
        this.movie = movie;
        this.actor = actor;
        this.isMain = isMain;
        movie.getMovieActorList().add(this);
        actor.getMovieActorList().add(this);
    }
}
