//package com.qinweizhao.blog.listener.post;
//
//import com.qinweizhao.blog.event.post.SheetVisitEvent;
//import com.qinweizhao.blog.service.SheetService;
//import org.springframework.context.event.EventListener;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
///**
// * Sheet visit event listener.
// *
// * @author johnniang
// * @date 19-4-24
// */
//@Component
//public class SheetVisitEventListener extends AbstractVisitEventListener {
//
//    protected SheetVisitEventListener(SheetService sheetService) {
//        super(sheetService);
//    }
//
//    @Async
//    @EventListener
//    public void onSheetVisitEvent(SheetVisitEvent event) throws InterruptedException {
//        handleVisitEvent(event);
//    }
//
//}
