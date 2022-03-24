package com.qinweizhao.blog.listener;

import com.qinweizhao.blog.config.properties.HaloProperties;
import com.qinweizhao.blog.model.properties.PrimaryProperties;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.ThemeService;
import com.qinweizhao.blog.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Collections;

/**
 * The method executed after the application is started.
 *
 * @author ryanwang
 * @author guqing
 * @date 2018-12-05
 */
@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    private HaloProperties haloProperties;

    @Autowired
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
        Path backupPath = Paths.get(haloProperties.getBackupDir());
        Path dataExportPath = Paths.get(haloProperties.getDataExportDir());

        try {
            if (Files.notExists(workPath)) {
                Files.createDirectories(workPath);
                log.info("Created work directory: [{}]", workPath);
            }

            if (Files.notExists(backupPath)) {
                Files.createDirectories(backupPath);
                log.info("Created backup directory: [{}]", backupPath);
            }

            if (Files.notExists(dataExportPath)) {
                Files.createDirectories(dataExportPath);
                log.info("Created data export directory: [{}]", dataExportPath);
            }
        } catch (IOException ie) {
            throw new RuntimeException("Failed to initialize directories", ie);
        }
    }
}
