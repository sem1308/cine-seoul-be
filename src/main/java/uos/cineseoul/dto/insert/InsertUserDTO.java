package uos.cineseoul.dto.insert;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uos.cineseoul.annotation.IdStartsWithEnglish;
import uos.cineseoul.utils.enums.UserRole;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class InsertUserDTO {
    @Size(max = 100, min = 0)
    @IdStartsWithEnglish
    private String id;

    // 입력 비밀번호 자리수 제한
    @Size(max = 16, min = 0)
    private String pw;

    @Size(max = 100, min = 0)
    private String name;

    @Size(max = 1, min = 1)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @NotNull
    @Size(max = 13, min = 13)
    private String residentNum;

    @NotNull
    @Size(max = 11, min = 10)
    private String phoneNum;
}
