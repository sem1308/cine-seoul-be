package uos.cineseoul.controller.movie;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.create.CreateGenreDTO;
import uos.cineseoul.dto.insert.InsertGenreDTO;
import uos.cineseoul.dto.response.PrintGenreDTO;
import uos.cineseoul.entity.movie.Genre;
import uos.cineseoul.service.movie.GenreService;

import javax.validation.Valid;

@RestController
@RequestMapping("/genre")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;
    @GetMapping("/{genreCode}")
    @Operation(description = "장르 정보를 조회한다.")
    public ResponseEntity<PrintGenreDTO> lookUpGenre(@PathVariable("genreCode") String genreCode) {
        Genre genre = genreService.findGenre(genreCode);
        return ResponseEntity.ok(new PrintGenreDTO(genre));
    }

    @PostMapping
    @Operation(description = "장르 정보를 등록한다.")
    public ResponseEntity<String> resister(@RequestBody @Valid CreateGenreDTO createGenreDTO) {

        String genreCode = genreService.insert(new InsertGenreDTO(createGenreDTO)).getGenreCode();
        return ResponseEntity.ok(genreCode);
    }
}
