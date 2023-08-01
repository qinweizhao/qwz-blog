package com.qinweizhao.blog.framework.listener;

import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.service.SettingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 应用程序启动后执行的方法。
 *
 * @author qinweizhao
 * @since 2018-12-05
 */
@Slf4j
@Configuration
@AllArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {

    private final MyBlogProperties myBlogProperties;

    private final SettingService settingService;

    @Override
    public void onApplicationEvent(@NotNull ApplicationStartedEvent event) {
        this.initDirectory();
        this.printStartInfo();
    }

    /**
     * 打印开始信息
     */
    private void printStartInfo() {
        String blogUrl = settingService.getBlogBaseUrl();
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, "Blog started at         ", blogUrl));
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, "Blog admin started at   ", blogUrl, "/", myBlogProperties.getAdminPath()));
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_YELLOW, "Blog has started successfully!"));
    }

    /**
     * 初始化目录
     */
    private void initDirectory() {
        Path workPath = Paths.get(myBlogProperties.getWorkDir());
        try {
            if (Files.notExists(workPath)) {
                Files.createDirectories(workPath);
                log.info("创建工作目录: [{}]", workPath);
            }
        } catch (IOException ie) {
            throw new RuntimeException("初始化目录失败", ie);
        }
    }
}
