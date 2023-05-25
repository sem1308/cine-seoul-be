package uos.cineseoul.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintUserDTO {
    private Long userNum;

    private String id;

    private String pw;

    private String name;

    private String residentNum;

    private String phoneNum;

    private Integer point;

    private String role;

    private LocalDateTime createdAt;
}
