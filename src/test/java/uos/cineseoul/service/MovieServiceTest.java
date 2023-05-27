package uos.cineseoul.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieServiceTest {

    @Test
    public void dateStringTest() {
        LocalDateTime now = LocalDateTime.now();
        String nowTypeString = String.format("%d%02d%02d",now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        System.out.println("nowTypeString = " + nowTypeString);
    }
}