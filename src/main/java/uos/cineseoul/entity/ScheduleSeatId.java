package uos.cineseoul.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleSeatId implements Serializable {
    private Long schedule;
    private Long seat;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleSeatId scheduleSeatId = (ScheduleSeatId) o;
        return Objects.equals(getSchedule(), scheduleSeatId.getSchedule()) && Objects.equals(getSeat(), scheduleSeatId.getSeat());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSchedule(), getSeat());
    }
}
