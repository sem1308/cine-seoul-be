package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uos.cineseoul.entity.User;
import uos.cineseoul.utils.enums.UserRole;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u.userNum from USERS u where u.id = :id")
    Optional<Long> findNumById(@Param("id") String id);

    @Query("select u from USERS u where u.id = :id")
    Optional<User> findByUserId(@Param("id") String id);
    List<User> findByPhoneNum(String phoneNum);
    Optional<User> findByPhoneNumAndRole(String phoneNum,UserRole role);
}