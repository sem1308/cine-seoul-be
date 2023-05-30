package uos.cineseoul.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.*;
import uos.cineseoul.entity.Actor;
import uos.cineseoul.service.ActorService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/actor")
public class ActorController {
    private final ActorService actorService;
    @GetMapping("/{num}")
    @Operation(description = "배우 정보를 조회한다.")
    public ResponseEntity<PrintActorDTO> lookUpGenre(@PathVariable("num") Long num) {
        Actor actor = actorService.findActor(num);
        return ResponseEntity.ok(new PrintActorDTO(actor));
    }

    @PostMapping
    @Operation(description = "배우 정보를 등록한다.")
    public ResponseEntity<Long> resister(@RequestBody @Valid CreateActorDTO createActorDTO) {

        Long actNum = actorService.insert(new InsertActorDTO(createActorDTO)).getActNum();
        return ResponseEntity.ok(actNum);
    }
}
