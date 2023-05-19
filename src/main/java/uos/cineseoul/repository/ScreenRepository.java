package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uos.cineseoul.entity.Screen;

import java.util.Optional;

public interface ScreenRepository extends JpaRepository<Screen, Long> {

    Optional<Screen> findByName(@Param("name") String name);
}