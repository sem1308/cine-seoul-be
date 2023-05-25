package uos.cineseoul.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintTicketDTO {
    private Long ticketNum;

    private Integer stdPrice;

    private Integer salePrice;

    private String issued;

    private LocalDateTime createdAt;

    private PrintUserDTO user;

    private PrintScheduleSeatDTO scheduleSeat;
}
