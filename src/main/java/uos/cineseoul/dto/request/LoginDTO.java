package uos.cineseoul.dto.request;

import lombok.Data;
import uos.cineseoul.annotation.IdStartsWithEnglish;

@Data
public class LoginDTO {
    @IdStartsWithEnglish
    String id;
    String pw;
}
