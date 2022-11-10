package com.qinweizhao.blog.framework.security.authentication;

import com.qinweizhao.blog.framework.security.support.UserDetail;
import org.springframework.lang.NonNull;

/**
 * Authentication.
 */
public interface Authentication {

    /**
     * Get user detail.
     *
     * @return user detail
     */
    @NonNull
    UserDetail getDetail();
}
