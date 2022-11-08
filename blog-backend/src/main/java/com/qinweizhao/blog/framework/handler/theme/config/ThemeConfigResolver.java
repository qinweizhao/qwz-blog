package com.qinweizhao.blog.framework.handler.theme.config;

import com.qinweizhao.blog.framework.handler.theme.config.support.Group;

import java.io.IOException;
import java.util.List;

/**
 * Theme config resolver interface.
 *
 * @author johnniang
 * @since 2019-04-10
 */
public interface ThemeConfigResolver {

    /**
     * Resolves content as group list.
     *
     * @param content content must not be blank
     * @return a list of group
     * @throws IOException throws when content conversion fails
     */
    List<Group> resolve(String content) throws IOException;

}
