package uos.cineseoul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertEventDTO;
import uos.cineseoul.dto.update.UpdateEventDTO;
import uos.cineseoul.entity.Event;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.EventRepository;
import uos.cineseoul.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    public Event insert(Long userNum, InsertEventDTO insertEventDTO) {
        Event event = Event.builder()
                .user(userRepository.findById(userNum).orElseThrow(
                        () -> new ResourceNotFoundException("해당하는 유저가 없습니다.")
                ))
                .image(insertEventDTO.getImage())
                .startAt(insertEventDTO.getStartAt())
                .endAt(insertEventDTO.getEndAt())
                .banner(insertEventDTO.getBanner())
                .contents(insertEventDTO.getContents())
                .title(insertEventDTO.getTitle())
                .build();
        return eventRepository.save(event);
    }

    public Event update(UpdateEventDTO updateEventDTO) {
        Event event = eventRepository.findById(updateEventDTO.getEventNum()).orElseThrow(
                () -> new ResourceNotFoundException("해당 이벤트를 찾을 수 없습니다.")
        );
        if (updateEventDTO.getContents() != null)
            event.setContents(updateEventDTO.getContents());
        if (updateEventDTO.getStartAt() != null)
            event.setStartAt(updateEventDTO.getStartAt());
        if (updateEventDTO.getEndAt() != null)
            event.setEndAt(updateEventDTO.getEndAt());
        if (updateEventDTO.getBanner() != null)
            event.setBanner(updateEventDTO.getBanner());
        if (updateEventDTO.getTitle()!= null)
            event.setTitle(updateEventDTO.getTitle());
        if (updateEventDTO.getImage() != null)
            event.setImage(updateEventDTO.getImage());
        if (updateEventDTO.getViews() != null)
            event.setViews(updateEventDTO.getViews());
        event.setCreatedAt(LocalDateTime.now());

        return eventRepository.save(event);
    }

    @Transactional
    public Event findEvent(Long eventNum) {
        Event event = eventRepository.findById(eventNum).orElseThrow(
                () -> new ResourceNotFoundException("해당 이벤트를 찾을 수 없습니다.")
        );
        event.increaseViews();
        return event;
    }

    public List<Event> findEventListNow(LocalDateTime localDateTime) {
        return eventRepository.findAllByNow(localDateTime);
    }

    public Page<Event> findEventList(Pageable pageable) {
        Page<Event> page = eventRepository.findAll(pageable);
        return page;
    }
}
