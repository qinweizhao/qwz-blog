package com.qinweizhao.blog.service;

/**
 * Data process service interface.
 *
 * @author ryanwang
 * @since 2019-12-29
 */
public interface DataProcessService {

    /**
     * Replace all url.
     *
     * @param oldUrl old url must not be null.
     * @param newUrl new url must not be null.
     */
    void replaceAllUrl(String oldUrl, String newUrl);
}
