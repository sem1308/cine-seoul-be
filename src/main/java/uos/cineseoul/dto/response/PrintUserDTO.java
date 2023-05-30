package uos.cineseoul.dto.response;

import lombok.*;

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

    private String role;

    private LocalDateTime createdAt;
}
