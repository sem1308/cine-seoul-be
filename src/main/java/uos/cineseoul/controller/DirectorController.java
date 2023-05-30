package uos.cineseoul.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.*;
import uos.cineseoul.entity.Director;
import uos.cineseoul.service.DirectorService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/director")
public class DirectorController {

    private final DirectorService directorService;
    @GetMapping("/{num}")
    @Operation(description = "감독 정보를 조회한다.")
    public ResponseEntity<PrintDirectorDTO> lookUpGenre(@PathVariable("num") Long num) {
        Director director = directorService.findDirector(num);
        return ResponseEntity.ok(new PrintDirectorDTO(director));
    }

    @PostMapping
    @Operation(description = "감독 정보를 등록한다.")
    public ResponseEntity<Long> resister(@RequestBody @Valid CreateDirectorDTO createDirectorDTO) {

        Long dirNum = directorService.insert(new InsertDirectorDTO(createDirectorDTO)).getDirNum();
        return ResponseEntity.ok(dirNum);
    }
}
