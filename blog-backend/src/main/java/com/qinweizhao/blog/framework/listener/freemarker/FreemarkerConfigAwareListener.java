package com.qinweizhao.blog.framework.listener.freemarker;

import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.framework.event.config.ConfigUpdatedEvent;
import com.qinweizhao.blog.framework.event.user.UserUpdatedEvent;
import com.qinweizhao.blog.model.constant.SystemConstant;
import com.qinweizhao.blog.model.support.BlogConst;
import com.qinweizhao.blog.service.SettingService;
import com.qinweizhao.blog.service.UserService;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author qinweizhao
 * @since 2022-03-15
 */
@Slf4j
@Component
@AllArgsConstructor
public class FreemarkerConfigAwareListener {

    private final SettingService settingService;

    private final Configuration configuration;

    private final UserService userService;
    private final MyBlogProperties myBlogProperties;

    @EventListener
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public void onApplicationStartedEvent(ApplicationStartedEvent applicationStartedEvent) throws TemplateModelException {
        log.debug("收到应用程序启动事件，时间：{}", applicationStartedEvent.getTimestamp());

        // 加载主题配置
        loadThemeConfig();
        loadOptionsConfig();
        loadUserConfig();
    }


    @EventListener
    public void onUserUpdate(UserUpdatedEvent event) throws TemplateModelException {
        log.debug("收到用户更新事件, 用户 id: [{}]", event.getUserId());

        loadUserConfig();
    }

    private void loadUserConfig() throws TemplateModelException {
        configuration.setSharedVariable("user", userService.getCurrentUser().orElse(null));
        log.debug("加载用户");
    }


    @EventListener
    public void onOptionUpdate(ConfigUpdatedEvent event) throws TemplateModelException {
        log.debug("收到配置更新事件时间：{}", event.getTimestamp());

        loadOptionsConfig();
        loadThemeConfig();
        loadUserConfig();
    }

    private void loadOptionsConfig() throws TemplateModelException {

        final String blogBaseUrl = settingService.getBlogBaseUrl();
        final String context = blogBaseUrl + "/";

        configuration.setSharedVariable("options", settingService.getSettings());
        configuration.setSharedVariable("context", context);
        configuration.setSharedVariable("version", BlogConst.HALO_VERSION);
        configuration.setSharedVariable("blog_title", settingService.get("blog_title"));
        configuration.setSharedVariable("blog_url", blogBaseUrl);
        configuration.setSharedVariable("blog_logo", settingService.get("blog_logo"));
        configuration.setSharedVariable("seo_keywords", settingService.get("seo_keywords"));
        configuration.setSharedVariable("seo_description", settingService.get("seo_description"));
        configuration.setSharedVariable("sitemap_xml_url", blogBaseUrl + "/sitemap.xml");
        configuration.setSharedVariable("sitemap_html_url", blogBaseUrl + "/sitemap.html");
        configuration.setSharedVariable("archives_url", context + SystemConstant.ARCHIVES_PREFIX);
        configuration.setSharedVariable("categories_url", context + SystemConstant.CATEGORIES_PREFIX);
        configuration.setSharedVariable("tags_url", context + SystemConstant.TAGS_PREFIX);

        log.debug("已加载选项");
    }

    private void loadThemeConfig() {

        String themeBasePath = settingService.getBlogBaseUrl() + "/themes/" + myBlogProperties.getThemeDirName();
        try {
//            configuration.setSharedVariable("theme", activatedTheme);

            configuration.setSharedVariable("static", themeBasePath);

            configuration.setSharedVariable("theme_base", themeBasePath);

            configuration.setSharedVariable("settings", settingService.getSettings());

            log.debug("加载主题和设置");
        } catch (TemplateModelException e) {

            log.error("设置共享变量失败!", e);

        }

    }
}
