package com.qinweizhao.blog.framework.event.options;

import org.springframework.context.ApplicationEvent;

/**
 * 配置更新
 *
 * @author qinweizhao
 * @since 2022/7/31
 */
public class ConfigUpdatedEvent extends ApplicationEvent {

    public ConfigUpdatedEvent(Object source) {
        super(source);
    }

}
