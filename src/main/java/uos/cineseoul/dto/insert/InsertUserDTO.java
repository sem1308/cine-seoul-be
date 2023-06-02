package uos.cineseoul.dto.insert;

import com.sun.istack.NotNull;
import lombok.*;
import uos.cineseoul.annotation.IdStartsWithEnglish;
import uos.cineseoul.annotation.PhoneNumCheck;
import uos.cineseoul.annotation.ResidentNumCheck;
import uos.cineseoul.utils.enums.UserRole;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Null;
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
    @Size(max = 16)
    private String pw;

    @Size(max = 100)
    @Null
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole role;

    @ResidentNumCheck
    private String residentNum;

    @NotNull
    @PhoneNumCheck
    private String phoneNum;
}
