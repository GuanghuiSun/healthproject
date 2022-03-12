package com.healthproject;

import com.healthproject.model.CheckItem;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.healthproject.mapper")
public class HealthProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthProjectApplication.class, args);

    }

}
