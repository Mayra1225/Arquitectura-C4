package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class ArquitecturaC4Application {
    public static void main(String[] args) {
        SpringApplication.run(ArquitecturaC4Application.class, args);
    }
}
