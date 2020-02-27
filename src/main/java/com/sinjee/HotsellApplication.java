package com.sinjee;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.sinjee.*.mapper")
@EnableCaching
@EnableScheduling   // 2.开启定时任务
public class HotsellApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotsellApplication.class, args);
    }

}
