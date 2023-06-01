package uos.cineseoul.service.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertDirectorDTO;
import uos.cineseoul.entity.movie.Director;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.DirectorRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class DirectorService {

    private final DirectorRepository directorRepository;

    public Director findDirector(Long dirNum) {
        Director director = directorRepository.findByDirNum(dirNum).orElseThrow(
                () -> new ResourceNotFoundException("해당 번호의 감독이 없습니다.")
        );
        return director;
    }

    public Director insert(InsertDirectorDTO insertDirectorDTO) {
        Director director = Director
                .builder()
                .name(insertDirectorDTO.getName())
                .imgUrl(insertDirectorDTO.getImgUrl())
                .build();
        return directorRepository.save(director);
    }
}
