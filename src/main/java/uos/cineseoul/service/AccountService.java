package uos.cineseoul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uos.cineseoul.entity.Account;
import uos.cineseoul.exception.ForbiddenException;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.AccountRepository;

@Service
public class AccountService {
    private  final AccountRepository accountRepo;

    @Autowired
    public AccountService(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    public void pay(String cusName, String cardNum, int price){
        Account account = accountRepo.findByCardNumAndOwnerName(cardNum,cusName)
                .orElseThrow(() -> new ResourceNotFoundException("카드 정보가 없습니다."));

        if (account.getBalance() >= price){
            account.setBalance(account.getBalance() - price);
            accountRepo.save(account);
        }else{
            throw new ForbiddenException("잔액이 부족합니다.");
        }
    }

    public void refund(String cusName, String cardNum, int price){
        Account account = accountRepo.findByCardNumAndOwnerName(cardNum,cusName)
                .orElseThrow(() -> new ResourceNotFoundException("카드 정보가 없습니다."));

        account.setBalance(account.getBalance() + price);
        accountRepo.save(account);
    }

    public void checkDuplicate(String cardNum,String cusName){
        if (accountRepo.findByCardNumAndOwnerName(cardNum,cusName).isEmpty()){
            throw new ResourceNotFoundException("카드 정보가 없습니다.");
        }
    }
}
