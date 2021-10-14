package com.zk.mybatisplus;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@MapperScan("com.zk.*.mapper") // 扫码mapper类，把bean加入到spring容器管理
@SpringBootApplication
public class SpringbootMybatisplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMybatisplusApplication.class, args);
        log.info("程序启动成功！");
        System.out.println("本地调试文档地址：http://localhost:8093/doc.html");
    }

}
