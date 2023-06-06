package uos.cineseoul.dto.create;

import lombok.*;

import java.util.List;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class CreateTicketDTO {
    private Long userNum;

    private Long schedNum;

    private Integer stdPrice;

    private List<Long> seatNumList;

    private List<CreateTicketAudienceDTO> audienceTypeDTOList;
}
