package com.example.boardserver;

import jdk.jfr.Enabled;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BoardServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardServerApplication.class, args);
    }

}
