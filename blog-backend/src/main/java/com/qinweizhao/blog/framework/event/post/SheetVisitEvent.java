package com.qinweizhao.blog.framework.event.post;

/**
 * Sheet visit event.
 *
 * @since 19-4-24
 */
public class SheetVisitEvent extends AbstractVisitEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source  the object on which the event initially occurred (never {@code null})
     * @param sheetId sheet id must not be null
     */
    public SheetVisitEvent(Object source, Integer sheetId) {
        super(source, sheetId);
    }
}
