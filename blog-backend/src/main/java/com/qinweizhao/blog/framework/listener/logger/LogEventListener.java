//package com.qinweizhao.blog.listener.logger;
//
//import com.qinweizhao.blog.event.logger.LogEvent;
//import com.qinweizhao.blog.service.LogService;
//import org.springframework.context.event.EventListener;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
///**
// * Log event listener.
// *
// * @author johnniang
// * @date 19-4-21
// */
//@Component
//public class LogEventListener {
//
//    private final LogService logService;
//
//    public LogEventListener(LogService logService) {
//        this.logService = logService;
//    }
//
//    @EventListener
//    @Async
//    public void onApplicationEvent(LogEvent event) {
//        // Convert to log
//        Log logToCreate = event.getLogParam().convertTo();
//
//        // Create log
//        logService.create(logToCreate);
//    }
//}
