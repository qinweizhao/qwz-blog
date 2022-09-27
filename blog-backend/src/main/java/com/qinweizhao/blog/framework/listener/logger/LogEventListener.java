package com.qinweizhao.blog.framework.listener.logger;

import com.qinweizhao.blog.framework.event.logger.LogEvent;
import com.qinweizhao.blog.model.convert.LogConvert;
import com.qinweizhao.blog.model.entity.Log;
import com.qinweizhao.blog.model.param.LogParam;
import com.qinweizhao.blog.service.LogService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author qinweizhao
 * @since 2022/9/27
 */
@Component
@AllArgsConstructor
public class LogEventListener {

    private final LogService logService;


    @EventListener
    @Async
    public void onApplicationEvent(LogEvent event) {
        LogParam param = event.getLogParam();

        Log log = LogConvert.INSTANCE.convert(param);

        logService.save(log);
    }
}
