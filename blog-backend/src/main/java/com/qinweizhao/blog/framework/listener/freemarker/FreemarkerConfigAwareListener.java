package com.qinweizhao.blog.framework.listener.freemarker;

import com.qinweizhao.blog.framework.event.options.OptionUpdatedEvent;
import com.qinweizhao.blog.framework.event.user.UserUpdatedEvent;
import com.qinweizhao.blog.model.properties.BlogProperties;
import com.qinweizhao.blog.model.properties.SeoProperties;
import com.qinweizhao.blog.model.support.HaloConst;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.ThemeService;
import com.qinweizhao.blog.service.ThemeSettingService;
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
 * @since 2022/7/4
 */
@Slf4j
@Component
@AllArgsConstructor
public class FreemarkerConfigAwareListener {

    private final OptionService optionService;

    private final Configuration configuration;

    private final ThemeService themeService;

    private final ThemeSettingService themeSettingService;

    private final UserService userService;

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
        log.debug("Loaded user");
    }


    @EventListener
    public void onOptionUpdate(OptionUpdatedEvent event) throws TemplateModelException {
        log.debug("收到配置更新事件时间：{}", event.getTimestamp());

        loadOptionsConfig();
        loadThemeConfig();
    }

    private void loadOptionsConfig() throws TemplateModelException {

        final String blogBaseUrl = optionService.getBlogBaseUrl();
        final String context = optionService.isEnabledAbsolutePath() ? blogBaseUrl + "/" : "/";

        configuration.setSharedVariable("options", optionService.listOptions());
        configuration.setSharedVariable("context", context);
        configuration.setSharedVariable("version", HaloConst.HALO_VERSION);
        configuration.setSharedVariable("globalAbsolutePathEnabled", optionService.isEnabledAbsolutePath());
        configuration.setSharedVariable("blog_title", optionService.getBlogTitle());
        configuration.setSharedVariable("blog_url", blogBaseUrl);
        configuration.setSharedVariable("blog_logo", optionService.getByPropertyOrDefault(BlogProperties.BLOG_LOGO, String.class, BlogProperties.BLOG_LOGO.defaultValue()));
        configuration.setSharedVariable("seo_keywords", optionService.getByPropertyOrDefault(SeoProperties.KEYWORDS, String.class, SeoProperties.KEYWORDS.defaultValue()));
        configuration.setSharedVariable("seo_description", optionService.getByPropertyOrDefault(SeoProperties.DESCRIPTION, String.class, SeoProperties.DESCRIPTION.defaultValue()));

        configuration.setSharedVariable("rss_url", blogBaseUrl + "/rss.xml");
        configuration.setSharedVariable("atom_url", blogBaseUrl + "/atom.xml");
        configuration.setSharedVariable("sitemap_xml_url", blogBaseUrl + "/sitemap.xml");
        configuration.setSharedVariable("sitemap_html_url", blogBaseUrl + "/sitemap.html");
        configuration.setSharedVariable("links_url", context + optionService.getLinksPrefix());
        configuration.setSharedVariable("photos_url", context + optionService.getPhotosPrefix());
        configuration.setSharedVariable("journals_url", context + optionService.getJournalsPrefix());
        configuration.setSharedVariable("archives_url", context + optionService.getArchivesPrefix());
        configuration.setSharedVariable("categories_url", context + optionService.getCategoriesPrefix());
        configuration.setSharedVariable("tags_url", context + optionService.getTagsPrefix());

        log.debug("已加载选项");
    }

    private void loadThemeConfig() {
        // 获取当前激活的主题
        themeService.fetchActivatedTheme().ifPresent(activatedTheme -> {
            String themeBasePath = (optionService.isEnabledAbsolutePath() ? optionService.getBlogBaseUrl() : "") + "/themes/" + activatedTheme.getFolderName();
            try {
                configuration.setSharedVariable("theme", activatedTheme);

                configuration.setSharedVariable("static", themeBasePath);

                configuration.setSharedVariable("theme_base", themeBasePath);

                configuration.setSharedVariable("settings", themeSettingService.getSettings());

                log.debug("加载主题和设置");
            } catch (TemplateModelException e) {

                log.error("设置共享变量失败!", e);

            }
        });

    }
}
