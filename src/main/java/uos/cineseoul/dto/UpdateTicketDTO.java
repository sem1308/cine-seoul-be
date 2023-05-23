package uos.cineseoul.dto;

import lombok.*;

import javax.validation.constraints.Size;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class UpdateTicketDTO {
    private Long ticketNum ;
    private Integer salePrice;

    @Size(max = 1, min = 1)
    private String issued;

    private Long schedNum;

    private Long seatNum;
}
