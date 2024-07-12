package studio.aroundhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UserProgramApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserProgramApplication.class, args);
    }
}