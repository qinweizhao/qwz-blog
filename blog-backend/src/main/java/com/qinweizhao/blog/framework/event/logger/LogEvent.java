package com.qinweizhao.blog.framework.event.logger;

import com.qinweizhao.blog.model.enums.LogType;
import com.qinweizhao.blog.model.param.LogParam;
import com.qinweizhao.blog.util.ServletUtils;
import com.qinweizhao.blog.util.ValidationUtils;
import org.springframework.context.ApplicationEvent;

/**
 * @author johnniang
 * @since 19-4-20
 */
public class LogEvent extends ApplicationEvent {

    private final LogParam logParam;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source   the object on which the event initially occurred (never {@code null})
     * @param logParam login param
     */
    public LogEvent(Object source, LogParam logParam) {
        super(source);

        ValidationUtils.validate(logParam);

        logParam.setIpAddress(ServletUtils.getRequestIp());

        this.logParam = logParam;
    }

    public LogEvent(Object source, String logKey, LogType logType, String content) {
        this(source, new LogParam(logKey, logType, content));
    }

    public LogParam getLogParam() {
        return logParam;
    }
}
