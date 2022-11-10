package com.qinweizhao.blog.framework.listener.post;

import com.qinweizhao.blog.framework.event.post.PostVisitEvent;
import com.qinweizhao.blog.service.PostService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Visit event listener.
 *
 * @since 19-4-22
 */
@Component
public class PostVisitEventListener extends AbstractVisitEventListener {

    public PostVisitEventListener(PostService postService) {
        super(postService);
    }

    @Async
    @EventListener
    public void onPostVisitEvent(PostVisitEvent event) throws InterruptedException {
        handleVisitEvent(event);
    }
}
