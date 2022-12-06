package com.qinweizhao.blog.framework.handler.theme.config.impl;

import com.qinweizhao.blog.framework.handler.theme.config.ThemePropertyResolver;
import com.qinweizhao.blog.framework.handler.theme.config.support.ThemeProperty;
import com.qinweizhao.blog.theme.YamlResolver;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * Yaml theme file resolver.
 * @author qinweizhoa
 * @since 2019-04-11
 */
@Service
public class YamlThemePropertyResolver implements ThemePropertyResolver {

    @Override
    @NonNull
    public ThemeProperty resolve(@NonNull String content) throws IOException {
        Assert.hasText(content, "Theme file content must not be null");

        return YamlResolver.INSTANCE.getYamlMapper().readValue(content, ThemeProperty.class);
    }
}
