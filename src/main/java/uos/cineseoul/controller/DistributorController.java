package uos.cineseoul.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.*;
import uos.cineseoul.entity.Distributor;
import uos.cineseoul.service.DistributorService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/distributor")
public class DistributorController {

    private final DistributorService distributorService;
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
