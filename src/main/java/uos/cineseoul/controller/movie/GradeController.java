package uos.cineseoul.controller.movie;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.create.CreateGradeDTO;
import uos.cineseoul.dto.insert.InsertGradeDTO;
import uos.cineseoul.dto.response.PrintGradeDTO;
import uos.cineseoul.entity.movie.Grade;
import uos.cineseoul.service.movie.GradeService;

import javax.validation.Valid;

@RestController
@RequestMapping("/grade")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;

    @GetMapping("/{gradeCode}")
    @Operation(description = "등급 정보를 조회한다.")
    public ResponseEntity<PrintGradeDTO> lookUpGenre(@PathVariable("gradeCode") String gradeCode) {
        Grade grade = gradeService.findGrade(gradeCode);
        return ResponseEntity.ok(new PrintGradeDTO(grade));
    }

    @PostMapping
    @Operation(description = "등급 정보를 등록한다.")
    public ResponseEntity<String> resister(@RequestBody @Valid CreateGradeDTO createGradeDTO) {

        String gradeCode = gradeService.insert(new InsertGradeDTO(createGradeDTO)).getGradeCode();
        return ResponseEntity.ok(gradeCode);
    }

}
