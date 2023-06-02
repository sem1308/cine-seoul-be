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
import uos.cineseoul.dto.create.CreateActorDTO;
import uos.cineseoul.dto.insert.InsertActorDTO;
import uos.cineseoul.dto.response.PrintActorDTO;
import uos.cineseoul.dto.response.PrintDirectorDTO;
import uos.cineseoul.dto.response.PrintPageDTO;
import uos.cineseoul.entity.movie.Actor;
import uos.cineseoul.entity.movie.Director;
import uos.cineseoul.service.movie.ActorService;
import uos.cineseoul.utils.enums.RunningType;
import uos.cineseoul.utils.enums.SortMovieBy;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/actor")
public class ActorController {
    private final ActorService actorService;

    @GetMapping()
    @Operation(description = "배우 목록을 조회한다.")
    public ResponseEntity<PrintPageDTO> lookUpGenreList(@RequestParam(value="sort_name", required = false) boolean isSortName,
                                                       @RequestParam(value="sort_dir", required = false) Sort.Direction sortDir,
                                                       @RequestParam(value="page", required = false, defaultValue = "0") int page,
                                                       @RequestParam(value="size", required = false, defaultValue = "12") int size) {
        Pageable pageable;
        if(sortDir==null) sortDir = Sort.Direction.ASC;
        String sortBy = "actNum";
        if(isSortName) sortBy = "name";
        if(sortDir.equals(Sort.Direction.ASC)){
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        }else{
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        Page<Actor> actorList = actorService.findActorList(pageable);
        List<PrintActorDTO> printActorDTOS = actorList
                .stream()
                .map(a -> new PrintActorDTO(a))
                .collect(Collectors.toList());
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("list", printActorDTOS);
        responseData.put("total_page", actorList.getTotalPages());
        return new ResponseEntity<>(new PrintPageDTO<>(printActorDTOS,actorList.getTotalPages()), HttpStatus.OK);
    }

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
