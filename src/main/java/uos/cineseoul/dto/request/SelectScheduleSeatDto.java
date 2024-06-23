package uos.cineseoul.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SelectScheduleSeatDto {
    Long scheduleNum;
    Long seatNum;
}
