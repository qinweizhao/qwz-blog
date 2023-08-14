package com.qinweizhao.blog.framework.event.article;

import com.qinweizhao.blog.util.ServiceUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * Post visit event.
 *
 * @author qinweizhao
 * @since 19-4-22
 */
public class ArticleVisitEvent extends AbstractVisitEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     * @param articleId post id must not be null
     */
    public ArticleVisitEvent(Object source, @NonNull Integer articleId) {
        super(source, articleId);
        Assert.isTrue(!ServiceUtils.isEmptyId(articleId), "Post id must not be empty");
    }
}
