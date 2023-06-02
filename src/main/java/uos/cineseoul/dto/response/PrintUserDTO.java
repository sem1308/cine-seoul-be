package uos.cineseoul.dto.response;

import lombok.*;
import uos.cineseoul.utils.enums.UserRole;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

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

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private LocalDateTime createdAt;
}
