package uos.cineseoul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
import uos.cineseoul.utils.JwtTokenProvider;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/{num}")
    public ResponseEntity<PrintEventDTO> lookUpDetailEvent(@PathVariable("num") Long num) {
        Event event = eventService.findEvent(num);
        return ResponseEntity.ok(new PrintEventDTO(event));
    }

    @GetMapping("/list")
    public ResponseEntity<PrintPageDTO<PrintEventDTO>> lookUpEventList(@RequestParam Integer page, @RequestParam Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> pageEvent = eventService.findEventList(pageable);
        List<PrintEventDTO> list = pageEvent.getContent()
                .stream()
                .map(PrintEventDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new PrintPageDTO<>(list, pageEvent.getTotalPages()));
    }

    @GetMapping
    public ResponseEntity<List<PrintEventDTO>> lookUpEventOngoingList() {
        List<Event> list = eventService.findEventListNow(LocalDateTime.now());
        List<PrintEventDTO> printEventDTOList = list.stream().map(PrintEventDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(printEventDTOList);
    }

    @PostMapping
    public ResponseEntity<Long> register(@RequestHeader(value = "Authorization") String token, @RequestBody @Valid CreateEventDTO createEventDTO) {
        Long userNum = jwtTokenProvider.getClaims(token).get("num", Long.class);
        return ResponseEntity.ok(eventService.insert(userNum, new InsertEventDTO(createEventDTO)).getEventNum());
    }

    @PutMapping
    public ResponseEntity<Long> update(@RequestBody @Valid FixEventDTO fixEventDTO) {
        return ResponseEntity.ok(eventService.update(new UpdateEventDTO(fixEventDTO)).getEventNum());
    }

}
