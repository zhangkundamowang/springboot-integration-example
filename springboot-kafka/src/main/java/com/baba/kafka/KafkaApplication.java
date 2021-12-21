package com.baba.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class, args);
        //zookeeper版本 zookeeper-3.5.9    windows
        //kafka版本 kafka_2.12-2.8.1       windows
        //项目版本2.5.6
        System.out.println("本地调试文档地址：http://localhost:8888/doc.html");
    }

}
