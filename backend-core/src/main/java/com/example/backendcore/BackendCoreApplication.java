package com.example.backendcore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 开启定时任务
@EnableAsync      // 开启异步支持 (给告警监听器用)
@MapperScan("com.example.backendcore.mapper")
public class BackendCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendCoreApplication.class, args);
    }

}
