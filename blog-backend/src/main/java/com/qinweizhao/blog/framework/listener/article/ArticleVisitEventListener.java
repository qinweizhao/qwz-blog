package com.qinweizhao.blog.framework.listener.article;

import com.qinweizhao.blog.framework.event.article.ArticleVisitEvent;
import com.qinweizhao.blog.service.ArticleService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Visit event listener.
 *
 * @author qinweizhao
 * @since 19-4-22
 */
@Component
public class ArticleVisitEventListener extends AbstractVisitEventListener {

    public ArticleVisitEventListener(ArticleService articleService) {
        super(articleService);
    }

    @Async
    @EventListener
    public void onPostVisitEvent(ArticleVisitEvent event) throws InterruptedException {
        handleVisitEvent(event);
    }
}
