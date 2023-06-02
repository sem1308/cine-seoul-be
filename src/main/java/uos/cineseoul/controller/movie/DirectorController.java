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
import uos.cineseoul.dto.create.CreateDirectorDTO;
import uos.cineseoul.dto.insert.InsertDirectorDTO;
import uos.cineseoul.dto.response.PrintDirectorDTO;
import uos.cineseoul.dto.response.PrintDistributorDTO;
import uos.cineseoul.dto.response.PrintPageDTO;
import uos.cineseoul.entity.movie.Director;
import uos.cineseoul.entity.movie.Distributor;
import uos.cineseoul.service.movie.DirectorService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/director")
public class DirectorController {


    private final DirectorService directorService;

    @GetMapping()
    @Operation(description = "감독 정보 목록을 조회한다.")
    public ResponseEntity<PrintPageDTO> lookUpGenreList(@RequestParam(value="sort_name", required = false) boolean isSortName,
                                                      @RequestParam(value="sort_dir", required = false) Sort.Direction sortDir,
                                                      @RequestParam(value="page", required = false, defaultValue = "0") int page,
                                                      @RequestParam(value="size", required = false, defaultValue = "12") int size) {
        Pageable pageable;
        if(sortDir==null) sortDir = Sort.Direction.ASC;
        String sortBy = "dirNum";
        if(isSortName) sortBy = "name";
        if(sortDir.equals(Sort.Direction.ASC)){
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        }else{
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }
        Page<Director> directorList = directorService.findDirectorList(pageable);
        List<PrintDirectorDTO> printDirectorDTOS = directorList
                .stream()
                .map(d -> new PrintDirectorDTO(d))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new PrintPageDTO<>(printDirectorDTOS,directorList.getTotalPages()), HttpStatus.OK);
    }
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
