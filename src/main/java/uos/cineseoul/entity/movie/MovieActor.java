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

    @Column(name = "CREDIT_ROLE", columnDefinition = "CHAR(1)", nullable = false)
    private String creditRole;

    @Builder
    public MovieActor(Movie movie, Actor actor, String creditRole) {
        this.movie = movie;
        this.actor = actor;
        this.creditRole = creditRole;
        movie.getMovieActorList().add(this);
        actor.getMovieActorList().add(this);
    }
}
