package uos.cineseoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uos.cineseoul.entity.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String>{
    public Optional<Account> findByCardNum(String cardNum);
    public Optional<Account> findByCardNumAndOwnerName(String cardNum, String ownerName);
    public Optional<Account> findByAccountNumAndOwnerName(String accountNum, String ownerName);
}
