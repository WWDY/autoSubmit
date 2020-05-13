package com.wdy.autosubmit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling//开启定时任务
@MapperScan("com.wdy.autosubmit.Dao")
public class AutosubmitApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutosubmitApplication.class, args);
    }

}
