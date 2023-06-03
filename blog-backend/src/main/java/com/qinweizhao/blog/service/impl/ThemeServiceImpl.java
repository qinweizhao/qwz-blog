package com.qinweizhao.blog.service.impl;

import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.exception.ServiceException;
import com.qinweizhao.blog.framework.handler.theme.config.ThemeConfigResolver;
import com.qinweizhao.blog.framework.handler.theme.config.support.Group;
import com.qinweizhao.blog.service.ThemeService;
import com.qinweizhao.blog.util.FileUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * Theme service implementation.
 *
 * @author qinweizhao
 * @since 2019-03-26
 */
@Slf4j
@Service
@AllArgsConstructor
public class ThemeServiceImpl implements ThemeService {

    private final MyBlogProperties myBlogProperties;

    private final ThemeConfigResolver themeConfigResolver;

    /**
     * @return Path
     */
    public Path getBasePath() {
        String frontendDirName = myBlogProperties.getFrontendDirName();
        String themeDirName = myBlogProperties.getThemeDirName();
        Path frontend = Paths.get(myBlogProperties.getWorkDir(), frontendDirName, themeDirName);
        log.debug("将要扫描的目录为：{}", frontend);
        return frontend;
    }

    @Override
    public boolean templateExists(String template) {
        if (StringUtils.isBlank(template)) {
            return false;
        }
        Path templatePath = Paths.get(this.getBasePath().toString(), template);
        // Check the directory
        checkDirectory(templatePath.toString());
        // Check existence
        return Files.exists(templatePath);

    }

    @Override
    public List<Group> listConfig() {

        try {
            for (String optionsName : SETTINGS_NAMES) {
                // Resolve the options path
                Path optionsPath = Paths.get(this.getBasePath().toString(), optionsName);

                log.debug("Finding options in: [{}]", optionsPath);

                // Check existence
                if (!Files.exists(optionsPath)) {
                    continue;
                }

                // Read the yaml file
                String optionContent = new String(Files.readAllBytes(optionsPath), StandardCharsets.UTF_8);

                // Resolve it
                return themeConfigResolver.resolve(optionContent);
            }

            return Collections.emptyList();
        } catch (IOException e) {
            throw new ServiceException("读取主题配置文件失败", e);
        }
    }

    @Override
    public String render(String pageName) {
        return String.format(RENDER_TEMPLATE, this.getBasePath().getFileName().toString(), pageName);

    }

    @Override
    public String renderWithSuffix(String pageName) {
        // 构建渲染地址
        return String.format(RENDER_TEMPLATE_SUFFIX, this.getBasePath().getFileName().toString(), pageName);
    }

    /**
     * 检查目录是否有效
     *
     * @param absoluteName must not be blank
     */
    private void checkDirectory(String absoluteName) {
        FileUtils.checkDirectoryTraversal(this.getBasePath().toString(), absoluteName);
    }

}
