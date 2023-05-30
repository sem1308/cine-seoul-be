package uos.cineseoul.service.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertGradeDTO;
import uos.cineseoul.entity.movie.Grade;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.GradeRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class GradeService {

    private final GradeRepository gradeRepository;

    public Grade findGrade(String gradeCode) {
        Grade grade = gradeRepository.findByGradeCode(gradeCode).orElseThrow(
                () -> new ResourceNotFoundException("해당 코드의 등급을 찾을 수 없습니다. ")
        );
        return grade;
    }

    public Grade insert(InsertGradeDTO insertGradeDTO) {
        Grade grade  = Grade
                .builder()
                .gradeCode(insertGradeDTO.getGradeCode())
                .name(insertGradeDTO.getName())
                .build();
        return gradeRepository.save(grade);
    }
}
