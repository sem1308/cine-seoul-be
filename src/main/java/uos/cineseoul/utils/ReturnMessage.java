package uos.cineseoul.utils;
import lombok.*;
import uos.cineseoul.utils.enums.StatusEnum;

@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class ReturnMessage<T> {
    private StatusEnum status;

    private String message;

    private T data;
}
