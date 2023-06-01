package uos.cineseoul.dto.request;

import lombok.Data;
import uos.cineseoul.annotation.IdStartsWithEnglish;

import javax.validation.Valid;

@Data
public class LoginDTO {
    @IdStartsWithEnglish
    String id;
    String pw;
}
