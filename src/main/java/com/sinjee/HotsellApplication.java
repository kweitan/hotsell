package com.sinjee;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sinjee.wechat.mapper")
public class HotsellApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotsellApplication.class, args);
    }

}
