package uos.cineseoul.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtil {
    public static Pageable setPageable(Integer page, Integer size, String sortBy, Sort.Direction sortDir){
        Pageable pageable = PageRequest.of(page, size);
        if(sortBy!=null){
            if(sortDir==null) sortDir=Sort.Direction.ASC;
            switch (sortDir){
                case DESC:
                    pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
                    break;
                case ASC:
                    pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
                    break;
            }
        }
        return pageable;
    }
}
