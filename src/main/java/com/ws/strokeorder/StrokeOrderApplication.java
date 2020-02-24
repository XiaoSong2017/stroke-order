package com.ws.strokeorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StrokeOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrokeOrderApplication.class, args);
    }
}
