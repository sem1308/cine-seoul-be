package uos.cineseoul.dto;

import lombok.Data;
import uos.cineseoul.entity.Actor;

@Data
public class PrintActorDTO {
    private Long actNum;
    private String name;

    public PrintActorDTO(Actor actor) {
        this.actNum = actor.getActNum();
        this.name = actor.getName();
    }
}
