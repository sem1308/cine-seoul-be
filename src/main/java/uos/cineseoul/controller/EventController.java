package uos.cineseoul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.create.CreateEventDTO;
import uos.cineseoul.dto.fix.FixEventDTO;
import uos.cineseoul.dto.insert.InsertEventDTO;
import uos.cineseoul.dto.response.PrintEventDTO;
import uos.cineseoul.dto.response.PrintPageDTO;
import uos.cineseoul.dto.update.UpdateEventDTO;
import uos.cineseoul.entity.Event;
import uos.cineseoul.service.EventService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;

    @GetMapping("/{num}")
    public ResponseEntity<PrintEventDTO> lookUpDetailEvent(@PathVariable("num") Long num) {
        Event event = eventService.findEvent(num);
        return ResponseEntity.ok(new PrintEventDTO(event));
    }

    @GetMapping
    public ResponseEntity<PrintPageDTO<PrintEventDTO>> lookUpEventList(@RequestParam Integer page, @RequestParam Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> pageEvent = eventService.findEventList(pageable);
        List<PrintEventDTO> list = pageEvent.getContent()
                .stream()
                .map(PrintEventDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new PrintPageDTO<>(list, pageEvent.getTotalPages()));
    }

    @PostMapping
    public ResponseEntity<Long> register(CreateEventDTO createEventDTO) {
        return ResponseEntity.ok(eventService.insert(new InsertEventDTO(createEventDTO)).getEventNum());
    }

    @PutMapping
    public ResponseEntity<Long> update(FixEventDTO fixEventDTO) {
        return ResponseEntity.ok(eventService.update(new UpdateEventDTO(fixEventDTO)).getEventNum());
    }

}
