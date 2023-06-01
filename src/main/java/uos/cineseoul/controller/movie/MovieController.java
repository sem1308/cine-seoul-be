package uos.cineseoul.controller.movie;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.create.CreateMovieDTO;
import uos.cineseoul.dto.fix.FixMovieDTO;
import uos.cineseoul.dto.insert.InsertMovieDTO;
import uos.cineseoul.dto.response.PrintDetailedMovieDTO;
import uos.cineseoul.dto.response.PrintMovieDTO;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.service.movie.MovieService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/{num}")
    @Operation(description = "영화 번호로 영화를 검색한다.")
    public ResponseEntity<PrintMovieDTO> lookUpMovieByNum(@PathVariable Long num) {
        Movie movie = movieService.findMovie(num);
        return ResponseEntity.ok(new PrintMovieDTO(movie));
    }

    @GetMapping("/search")
    @Operation(description = "영화 제목으로 영화를 검색한다.")
    public ResponseEntity<PrintMovieDTO> lookUpMovieByNum(@RequestParam("title") String title) {
        Movie movie = movieService.findMovieByTitle(title);
        return ResponseEntity.ok(new PrintMovieDTO(movie));
    }

    @GetMapping
    @Operation(description = "전체 영화 목록을 조회한다.")
    public ResponseEntity<List<PrintMovieDTO>> lookUpMovieList() {
        List<Movie> movieList = movieService.findAllMovie();
        List<PrintMovieDTO> printMovieDTOS = new ArrayList<>();
        movieList.forEach(
                movie -> printMovieDTOS.add(new PrintMovieDTO(movie))
        );
        return ResponseEntity.ok(printMovieDTOS);
    }

    @GetMapping("/showing")
    @Operation(description = "상영중인 영화 목록을 조회한다.")
    public ResponseEntity<List<PrintMovieDTO>> lookUpShowingMovieList() {
        List<Movie> movieList = movieService.findAllShowingMovie();
        List<PrintMovieDTO> printMovieDTOList = movieList
                .stream()
                .map(m -> new PrintMovieDTO(m))
                .collect(Collectors.toList());
        return ResponseEntity.ok(printMovieDTOList);
    }

    @GetMapping("/upcoming")
    @Operation(description = "상영예정인 영화 목록을 조회한다.")
    public ResponseEntity<List<PrintMovieDTO>> lookUpUpcomingMovieList() {
        List<Movie> movieList = movieService.findAllWillReleaseMovie();
        List<PrintMovieDTO> printMovieDTOList = movieList
                .stream()
                .map(m -> new PrintMovieDTO(m))
                .collect(Collectors.toList());
        return ResponseEntity.ok(printMovieDTOList);
    }

    @GetMapping("/{num}/detail")
    @Operation(description = "영화 상세 정보를 조회한다.")
    public ResponseEntity<PrintDetailedMovieDTO> lookUpDetailedMovieByNum(@PathVariable("num") Long num) {
        Movie movie = movieService.findMovie(num);
        return ResponseEntity.ok(new PrintDetailedMovieDTO(movie));
    }

    @PostMapping
    @Operation(description = "영화를 등록한다.")
    public ResponseEntity<Long> register(@RequestBody @Valid CreateMovieDTO createMovieDTO) {
        InsertMovieDTO insertMovieDTO = new InsertMovieDTO(createMovieDTO);
        Long movieNum = movieService.insert(insertMovieDTO).getMovieNum();
        return ResponseEntity.ok(movieNum);
    }

    @PutMapping
    @Operation(description = "영화를 수정한다.")
    public ResponseEntity<Long> update(@RequestBody @Valid FixMovieDTO fixMovieDTO) {

        return ResponseEntity.ok(1L);
    }
}
