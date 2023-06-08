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
                .Contents(insertEventDTO.getContents())
                .IMAGE(insertEventDTO.getIMAGE())
                .build();
        return eventRepository.save(event);
    }

    public Event update(UpdateEventDTO updateEventDTO) {
        Event event = eventRepository.findById(updateEventDTO.getEventNum()).orElseThrow(
                () -> new ResourceNotFoundException("해당 이벤트를 찾을 수 없습니다.")
        );
        if (updateEventDTO.getContents() != null)
            event.setContents(updateEventDTO.getContents());
        if (updateEventDTO.getIMAGE() != null)
            event.setIMAGE(updateEventDTO.getIMAGE());
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

    public Page<Event> findEventList(Pageable pageable) {
        Page<Event> page = eventRepository.findAll(pageable);
        return page;
    }
}
