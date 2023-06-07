package uos.cineseoul.dto.create;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class CreateTicketDTO {
    @NotNull
    private Long userNum;

    @NotNull
    private Long schedNum;

    @NotNull
    private Integer stdPrice;

    @NotNull
    private List<Long> seatNumList;

    @NotNull
    private List<CreateTicketAudienceDTO> audienceTypeDTOList;
}
