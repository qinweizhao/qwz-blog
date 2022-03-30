package com.qinweizhao.blog.listener.theme;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.qinweizhao.blog.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.event.options.OptionUpdatedEvent;
import com.qinweizhao.blog.event.theme.ThemeUpdatedEvent;
import com.qinweizhao.blog.service.ThemeService;

import javax.annotation.Resource;

/**
 * Theme updated listener.
 *
 * @author johnniang
 * @date 19-4-29
 */
@Component
public class ThemeUpdatedListener {

    @Resource
    private  AbstractStringCacheStore cacheStore;

    @EventListener
    public void onApplicationEvent(ThemeUpdatedEvent event) {
        cacheStore.delete(ThemeService.THEMES_CACHE_KEY);
    }

    @EventListener
    public void onOptionUpdatedEvent(OptionUpdatedEvent optionUpdatedEvent) {
        cacheStore.delete(ThemeService.THEMES_CACHE_KEY);
    }
}
