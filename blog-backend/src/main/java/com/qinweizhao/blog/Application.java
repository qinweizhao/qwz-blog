package com.qinweizhao.blog;

import com.qinweizhao.blog.repository.base.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * @author ryanwang
 * @author qinweizhao
 * @date 2017-11-14
 */
@SpringBootApplication(exclude = {MultipartAutoConfiguration.class})
@EnableAsync
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.qinweizhao.blog.repository", repositoryBaseClass = BaseRepositoryImpl.class)
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

}
