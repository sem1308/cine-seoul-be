package uos.cineseoul.utils;
import lombok.*;

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
