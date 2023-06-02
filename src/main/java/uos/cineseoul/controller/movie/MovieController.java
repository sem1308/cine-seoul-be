package uos.cineseoul.controller.movie;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.create.CreateMovieDTO;
import uos.cineseoul.dto.fix.FixMovieDTO;
import uos.cineseoul.dto.insert.InsertMovieDTO;
import uos.cineseoul.dto.response.PrintDetailedMovieDTO;
import uos.cineseoul.dto.response.PrintMovieDTO;
import uos.cineseoul.dto.response.PrintPageDTO;
import uos.cineseoul.dto.response.PrintPaymentDTO;
import uos.cineseoul.dto.update.UpdateMovieDTO;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.service.movie.MovieService;
import uos.cineseoul.utils.enums.RunningType;
import uos.cineseoul.utils.enums.SortMovieBy;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<PrintMovieDTO> lookUpMovieByTitle(@RequestParam("title") String title) {
        Movie movie = movieService.findMovieByTitle(title);
        return ResponseEntity.ok(new PrintMovieDTO(movie));
    }

    @GetMapping()
    @Operation(description = "특정 조건으로 영화 목록을 조회한다.")
    public ResponseEntity<PrintPageDTO<PrintMovieDTO>> lookUpMovieList(@RequestParam(value="type", required = true) RunningType type,
                                                               @RequestParam(value="sort_by", required = false) SortMovieBy sortBy,
                                                               @RequestParam(value="sort_dir", required = false) Sort.Direction sortDir,
                                                               @RequestParam(value="genre", required = false) String genre,
                                                               @RequestParam(value="page", required = false, defaultValue = "0") int page,
                                                               @RequestParam(value="size", required = false, defaultValue = "12") int size) {
        Pageable pageable;
        if(sortDir==null) sortDir = Sort.Direction.ASC;
        if(sortBy==null) sortBy = SortMovieBy.MOVIENUM;
        if(sortDir.equals(Sort.Direction.ASC)){
            pageable = PageRequest.of(page, size, Sort.by(sortBy.getFieldName()).ascending());
        }else{
            pageable = PageRequest.of(page, size, Sort.by(sortBy.getFieldName()).descending());
        }
        Page<Movie> movieList = null;
        switch (type){
            case showing:
                movieList = movieService.findAllShowingMovie(pageable);
                break;
            case upcomming:
                movieList = movieService.findAllWillReleaseMovie(pageable);
                break;
            case all:
                movieList = movieService.findAllMovie(pageable, genre);
                break;
        }
        List<PrintMovieDTO> printMovieDTOS = new ArrayList<>();
        movieList.getContent().forEach(
                movie -> printMovieDTOS.add(new PrintMovieDTO(movie))
        );
        return new ResponseEntity<>(new PrintPageDTO<>(printMovieDTOS,movieList.getTotalPages()), HttpStatus.OK);
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
        UpdateMovieDTO updateMovieDTO = new UpdateMovieDTO(fixMovieDTO);
        Long movieNum = movieService.update(updateMovieDTO).getMovieNum();
        return ResponseEntity.ok(movieNum);
    }
}
