package uos.cineseoul.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
@AllArgsConstructor()
public class PrintPageDTO<T> {
    List<T> list;
    Integer totalPages;
}
