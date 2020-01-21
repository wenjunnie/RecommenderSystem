package com.wenjun.recsys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.wenjun.recsys"})
@MapperScan("com.wenjun.recsys.dao")
@EnableScheduling
public class RecsysApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecsysApplication.class, args);
    }

}
