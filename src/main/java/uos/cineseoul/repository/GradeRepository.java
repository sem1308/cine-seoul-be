package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.Grade;

import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, String> {
    Optional<Grade> findByGradeCode(String gradeCode);
}
