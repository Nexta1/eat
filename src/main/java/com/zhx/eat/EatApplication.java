package com.zhx.eat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@SpringBootApplication
@MapperScan("com.zhx.eat.mapper")
@ServletComponentScan

public class EatApplication {

    public static void main(String[] args) {
        SpringApplication.run(EatApplication.class, args);
    }


}
