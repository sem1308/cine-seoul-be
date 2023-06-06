package uos.cineseoul.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum ActorRole {
    M("주연"),
    E("조연"),
    S("특별출연");

    private String name;
}
