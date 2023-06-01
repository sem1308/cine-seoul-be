package uos.cineseoul.service.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertActorDTO;
import uos.cineseoul.entity.movie.Actor;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.ActorRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ActorService {
    private final ActorRepository actorRepository;

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
