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
import uos.cineseoul.dto.create.CreateDistributorDTO;
import uos.cineseoul.dto.insert.InsertDistributorDTO;
import uos.cineseoul.dto.response.*;
import uos.cineseoul.dto.response.PrintDistributorDTO;
import uos.cineseoul.entity.movie.Director;
import uos.cineseoul.entity.movie.Distributor;
import uos.cineseoul.entity.movie.Grade;
import uos.cineseoul.service.movie.DistributorService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/distributor")
public class DistributorController {

    private final DistributorService distributorService;


    @GetMapping()
    @Operation(description = "감독 정보 목록을 조회한다.")
    public ResponseEntity<PrintPageDTO> lookUpGenreList(@RequestParam(value="sort_name", required = false) boolean isSortName,
                                                       @RequestParam(value="sort_dir", required = false) Sort.Direction sortDir,
                                                       @RequestParam(value="page", required = false, defaultValue = "0") int page,
                                                       @RequestParam(value="size", required = false, defaultValue = "12") int size) {
        Pageable pageable;
        if(sortDir==null) sortDir = Sort.Direction.ASC;
        String sortBy = "distNum";
        if(isSortName) sortBy = "name";
        if(sortDir.equals(Sort.Direction.ASC)){
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        }else{
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }
        Page<Distributor> distributorList = distributorService.findDistributorList(pageable);
        List<PrintDistributorDTO> printDistributorDTOS = distributorList
                .stream()
                .map(d -> new PrintDistributorDTO(d))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new PrintPageDTO<>(printDistributorDTOS,distributorList.getTotalPages()), HttpStatus.OK);
    }

    @GetMapping("/{num}")
    @Operation(description = "배급사 정보를 조회한다.")
    public ResponseEntity<PrintDistributorDTO> lookUpGenre(@PathVariable("num") Long num) {
        Distributor distributor = distributorService.findDistributor(num);
        return ResponseEntity.ok(new PrintDistributorDTO(distributor));
    }

    @PostMapping
    @Operation(description = "배급사 정보를 등록한다.")
    public ResponseEntity<Long> resister(@RequestBody @Valid CreateDistributorDTO createDistributorDTO) {

        Long distNum = distributorService.insert(new InsertDistributorDTO(createDistributorDTO)).getDistNum();
        return ResponseEntity.ok(distNum);
    }
}
