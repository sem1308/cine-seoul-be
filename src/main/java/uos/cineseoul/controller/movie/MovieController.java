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
import uos.cineseoul.dto.update.UpdateMovieDTO;
import uos.cineseoul.entity.movie.Movie;
import uos.cineseoul.service.movie.MovieService;
import uos.cineseoul.utils.PageUtil;
import uos.cineseoul.utils.enums.request.RunningType;
import uos.cineseoul.utils.enums.request.SortMovieBy;

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

//    @GetMapping("/search")
//    @Operation(description = "영화 제목으로 영화를 검색한다.")
//    public ResponseEntity<PrintMovieDTO> lookUpMovieByTitle(@RequestParam("title") String title) {
//        Movie movie = movieService.findMovieByTitle(title);
//        return ResponseEntity.ok(new PrintMovieDTO(movie));
//    }

    @GetMapping("search")
    @Operation(description = "영화 제목으로 영화를 검색하며 유사한 결과값도 표시합니다.")
    public ResponseEntity<List<PrintMovieDTO>> lookUpMovieByTitle(@RequestParam("title") String title) {
        List<PrintMovieDTO> printMovieDTOList = movieService.findAllMovieLike(title).stream()
                .map(PrintMovieDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(printMovieDTOList);
    }

    @GetMapping("/test")
    public void l(@RequestParam String country)
    {
        Pageable pageable = PageRequest.of(0, 12);
        Page<Movie> movies = movieService.findAllCountryMovie(pageable, country);
        System.out.println("movies.getContent().get(0) = " + movies.getContent().get(0));
    }


    @GetMapping()
    @Operation(description = "특정 조건으로 영화 목록을 조회한다.")
    public ResponseEntity<PrintPageDTO<PrintMovieDTO>> lookUpMovieList(@RequestParam(value="type", required = false) RunningType type,
                                                               @RequestParam(value="sort_by", required = false) SortMovieBy sortBy,
                                                               @RequestParam(value="sort_dir", required = false) Sort.Direction sortDir,
                                                               @RequestParam(value="genre", required = false) String genre,
                                                               @RequestParam(value="page", required = false, defaultValue = "0") Integer page,
                                                               @RequestParam(value="size", required = false, defaultValue = "12") Integer size) {
        Pageable pageable = PageUtil.setPageable(page, size,sortBy==null?null:sortBy.getFieldName(),sortDir);
        Page<Movie> movieList;
        if(type==null)type=RunningType.all;
        switch (type){
            case showing:
                movieList = movieService.findAllShowingMovie(pageable, genre);
                break;
            case upcomming:
                movieList = movieService.findAllWillReleaseMovie(pageable, genre);
                break;
            case all:
            default:
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
