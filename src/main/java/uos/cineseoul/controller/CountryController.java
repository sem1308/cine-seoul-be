package uos.cineseoul.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.create.CreateCountryDTO;
import uos.cineseoul.dto.insert.InsertCountryDTO;
import uos.cineseoul.service.CountryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/country")
public class CountryController {
    private final CountryService countryService;

    @PostMapping()
    @Operation(description = "나라 정보를 등록한다.")
    public ResponseEntity<String> resister(@RequestBody @Valid CreateCountryDTO createCountryDTO) {
        String countryCode = countryService.insert(new InsertCountryDTO(createCountryDTO)).getCountryCode();
        return ResponseEntity.ok(countryCode);
    }
}
