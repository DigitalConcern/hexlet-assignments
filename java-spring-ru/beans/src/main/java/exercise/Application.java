package exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

import exercise.daytime.Daytime;
import exercise.daytime.Day;
import exercise.daytime.Night;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Scope("prototype")
    public Daytime daytime() {
        if (LocalDateTime.now().getHour() >= 10 && LocalDateTime.now().getHour() <= 22) {
            return new Day();
        } else {
            return new Night();
        }
    }
}
