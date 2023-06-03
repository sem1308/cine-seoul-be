package uos.cineseoul.dto.request;

import lombok.Data;
import uos.cineseoul.annotation.IdStartsWithEnglish;
import uos.cineseoul.annotation.PhoneNumCheck;

@Data
public class LoginDTO {
    @IdStartsWithEnglish
    String id;
    String pw;
}
