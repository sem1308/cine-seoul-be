package uos.cineseoul.service.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertActorDTO;
import uos.cineseoul.entity.movie.Actor;
import uos.cineseoul.entity.movie.Director;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.ActorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ActorService {
    private final ActorRepository actorRepository;

    public List<Actor> findActorList() {
        List<Actor> actorList = actorRepository.findAll();
        return actorList;
    }

    public Page<Actor> findActorList(Pageable pageable) {
        Page<Actor> actorList = actorRepository.findAll(pageable);
        return actorList;
    }

    public Actor findActor(Long actNum) {
        Actor actor = actorRepository.findByActNum(actNum).orElseThrow(
                () -> new ResourceNotFoundException("해당 번호의 배우가 없습니다. ")
        );
        return actor;
    }

    public Actor insert(InsertActorDTO insertActorDTO) {
        Actor actor = Actor
                .builder()
                .name(insertActorDTO.getName())
                .imgUrl(insertActorDTO.getImgUrl())
                .build();
        return actorRepository.save(actor);
    }
}
