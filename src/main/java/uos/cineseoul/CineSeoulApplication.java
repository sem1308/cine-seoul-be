package uos.cineseoul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 스케줄링 활성화
public class CineSeoulApplication {
	public static void main(String[] args) {
		SpringApplication.run(CineSeoulApplication.class, args);
	}
}
