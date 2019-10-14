package com.ncu.oa.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/9/26 0026
 * Time:10:20
 */
@SpringBootApplication
//开启定时任务
@EnableScheduling
@MapperScan(value = "com.ncu.oa.admin.mapper")
public class OAAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(OAAdminApplication.class, args);
    }
}
