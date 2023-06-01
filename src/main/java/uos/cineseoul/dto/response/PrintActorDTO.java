package uos.cineseoul.dto.response;

import lombok.Data;
import uos.cineseoul.entity.movie.Actor;

@Data
public class PrintActorDTO {
    private Long actNum;
    private String name;
    private String imgUrl;

    public PrintActorDTO(Actor actor) {
        this.actNum = actor.getActNum();
        this.name = actor.getName();
        this.imgUrl = actor.getImgUrl();
    }
}
