package com.qinweizhao.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author qinweizhao
 * @since 2022/7/4
 */
@SpringBootApplication(exclude = {MultipartAutoConfiguration.class})
@EnableAsync
@EnableScheduling
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {

        // 生产环境设置配置文件存放地址
        System.setProperty("spring.config.additional-location",
                "optional:file:${user.home}/.blog/");

        SpringApplication.run(Application.class, args);
    }

}
