package uos.cineseoul.controller.movie;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.create.CreateActorDTO;
import uos.cineseoul.dto.insert.InsertActorDTO;
import uos.cineseoul.dto.response.PrintActorDTO;
import uos.cineseoul.dto.response.PrintPageDTO;
import uos.cineseoul.entity.movie.Actor;
import uos.cineseoul.service.movie.ActorService;
import uos.cineseoul.utils.PageUtil;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/actor")
public class ActorController {
    private final ActorService actorService;

    @GetMapping()
    @Operation(description = "배우 목록을 조회한다.")
    public ResponseEntity<PrintPageDTO<PrintActorDTO>> lookUpGenreList(@RequestParam(value="sort_name", required = false) boolean isSortName,
                                                       @RequestParam(value="sort_dir", required = false) Sort.Direction sortDir,
                                                       @RequestParam(value="page", required = false, defaultValue = "0") int page,
                                                       @RequestParam(value="size", required = false, defaultValue = "12") int size) {
        String sortBy = isSortName? "name" : "actNum";
        Pageable pageable = PageUtil.setPageable(page, size,sortBy,sortDir);

        Page<Actor> actorList = actorService.findActorList(pageable);
        List<PrintActorDTO> printActorDTOS = actorList
                .stream()
                .map(a -> new PrintActorDTO(a))
                .collect(Collectors.toList());
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
