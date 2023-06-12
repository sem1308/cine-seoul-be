package uos.cineseoul.entity.movie;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uos.cineseoul.utils.enums.ActorRole;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Builder
@IdClass(MovieActorId.class)
public class MovieActor {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_NUM")
    private Movie movie;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACT_NUM")
    private Actor actor;

    @Column(name = "CAST_ROLE", columnDefinition = "CHAR(1)", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActorRole castRole;

    @Builder
    public MovieActor(Movie movie, Actor actor, ActorRole castRole) {
        this.movie = movie;
        this.actor = actor;
        this.castRole = castRole;
        movie.getMovieActorList().add(this);
        actor.getMovieActorList().add(this);
    }
}
