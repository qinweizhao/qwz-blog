package com.qinweizhao.blog.listener;

import com.qinweizhao.blog.config.properties.HaloProperties;
import com.qinweizhao.blog.service.OptionService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 应用程序启动后执行的方法。
 *
 * @author ryanwang
 * @author guqing
 * @author qinweizhao
 * @date 2018-12-05
 */
@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {

    @Resource
    private HaloProperties haloProperties;

    @Resource
    private OptionService optionService;

    @Override
    public void onApplicationEvent(@NotNull ApplicationStartedEvent event) {
        this.initDirectory();
        this.printStartInfo();
    }

    private void printStartInfo() {
        String blogUrl = optionService.getBlogBaseUrl();
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, "Blog started at         ", blogUrl));
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, "Blog admin started at   ", blogUrl, "/", haloProperties.getAdminPath()));
        if (!haloProperties.isDocDisabled()) {
            log.debug(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, "Blog api doc was enabled at  ", blogUrl, "/swagger-ui.html"));
        }
        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_YELLOW, "Blog has started successfully!"));
    }

    private void initDirectory() {
        Path workPath = Paths.get(haloProperties.getWorkDir());
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
