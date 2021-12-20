package com.baba.thymeleaf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.baba.thymeleaf.dao")
public class SpringbootThymeleafExcelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootThymeleafExcelApplication.class, args);
    }

}
