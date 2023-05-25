package uos.cineseoul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uos.cineseoul.entity.Account;
import uos.cineseoul.exception.ForbiddenException;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.AccountRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public void checkVaildity(String cardNum,String cusName){
        if (accountRepo.findByCardNumAndOwnerName(cardNum,cusName).isEmpty()){
            throw new ResourceNotFoundException("카드 정보가 없습니다.");
        }
    }

    public String getApprovalNum(String cardNum){
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss")).toString();
        // 카드사가 1개밖에 없다 가정, 7번째 자리부터는 고유하므로 그것만 가져옴
        Long approvalNumL = Long.parseLong(time+cardNum.substring(6));
        String approvalNum = Long.toHexString(approvalNumL).toUpperCase();

        return approvalNum;
    }
}
