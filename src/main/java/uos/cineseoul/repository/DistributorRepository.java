package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uos.cineseoul.entity.Distributor;

import java.util.Optional;

public interface DistributorRepository extends JpaRepository<Distributor, Long> {
    Optional<Distributor> findByDistNum(Long distNum);
}
