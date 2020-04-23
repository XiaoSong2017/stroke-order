package com.ws.strokeorder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author wangsong
 */
@SpringBootApplication
@EnableCaching
@MapperScan(basePackages = "com.ws.strokeorder.mapper")
public class StrokeOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrokeOrderApplication.class, args);
    }
}
