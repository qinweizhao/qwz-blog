package com.qinweizhao.blog.service.impl;

import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.exception.NotFoundException;
import com.qinweizhao.blog.exception.ServiceException;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.framework.handler.theme.config.ThemeConfigResolver;
import com.qinweizhao.blog.framework.handler.theme.config.support.Group;
import com.qinweizhao.blog.framework.handler.theme.config.support.ThemeProperty;
import com.qinweizhao.blog.mapper.ThemeSettingMapper;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.ThemeService;
import com.qinweizhao.blog.theme.ThemePropertyScanner;
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
import java.util.Optional;

import static com.qinweizhao.blog.model.support.HaloConst.DEFAULT_ERROR_PATH;

/**
 * Theme service implementation.
 *
 * @author ryanwang
 * @author qinweizhao
 * @since 2019-03-26
 */
@Slf4j
@Service
@AllArgsConstructor
public class ThemeServiceImpl implements ThemeService {

    private final OptionService optionService;

    private final MyBlogProperties myBlogProperties;

    private final ThemeConfigResolver themeConfigResolver;

    private final AbstractStringCacheStore cacheStore;

    private final ThemeSettingMapper themeSettingMapper;


    @Override
    public ThemeProperty getThemeProperty() {
        return Optional.of(getThemes()).orElseThrow(() -> new NotFoundException(" 主题不存在或已删除！"));
    }

    @Override
    public Optional<ThemeProperty> fetchActivatedTheme() {
        return Optional.of(getThemes());
    }

    /**
     * 获取主题配置
     *
     * @return List
     */
    public ThemeProperty getThemes() {

        return cacheStore.getAny(THEMES_CACHE_KEY, ThemeProperty.class).orElseGet(() -> {
            // 扫描配置，为防止报异常，如果存在多个只会取扫描的第一个。
            ThemeProperty properties = ThemePropertyScanner.INSTANCE.scan(getBasePath());
            // Cache the themes
            cacheStore.putAny(THEMES_CACHE_KEY, properties);
            return properties;
        });
    }

    /**
     * todo
     *
     * @return Path
     */
    public Path getBasePath() {
        if (myBlogProperties.isProductionEnv()) {
            return Paths.get(myBlogProperties.getWorkDir(), "theme");
        } else {
            return Paths.get(myBlogProperties.getWorkDir(), "blog-frontend");
        }
    }

    @Override
    public boolean templateExists(String template) {
        if (StringUtils.isBlank(template)) {
            return false;
        }

        return fetchActivatedTheme().map(themeProperty -> {
            // Resolve template path
            Path templatePath = Paths.get(themeProperty.getThemePath(), template);
            // Check the directory
            checkDirectory(templatePath.toString());
            // Check existence
            return Files.exists(templatePath);
        }).orElse(false);
    }

    @Override
    public List<Group> listConfig() {

        // 获取主题属性
        ThemeProperty themeProperty = getThemeProperty();

        if (!themeProperty.isHasOptions()) {
            // If this theme dose not has an option, then return empty list
            return Collections.emptyList();
        }

        try {
            for (String optionsName : SETTINGS_NAMES) {
                // Resolve the options path
                Path optionsPath = Paths.get(themeProperty.getThemePath(), optionsName);

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
        return fetchActivatedTheme()
                .map(themeProperty -> String.format(RENDER_TEMPLATE, themeProperty.getFolderName(), pageName))
                .orElse(DEFAULT_ERROR_PATH);
    }

    @Override
    public String renderWithSuffix(String pageName) {
        // 获取主题属性
        ThemeProperty activatedTheme = getThemeProperty();
        // 构建渲染地址
        return String.format(RENDER_TEMPLATE_SUFFIX, activatedTheme.getFolderName(), pageName);
    }

    /**
     * 检查目录是否有效
     *
     * @param absoluteName must not be blank
     */
    private void checkDirectory(String absoluteName) {
        ThemeProperty activeThemeProperty = getThemeProperty();
        FileUtils.checkDirectoryTraversal(activeThemeProperty.getThemePath(), absoluteName);
    }

}
