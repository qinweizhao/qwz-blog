package com.qinweizhao.blog.security.authentication;

import com.qinweizhao.blog.security.support.UserDetail;
import org.springframework.lang.NonNull;

/**
 * Authentication.
 *
 * @author johnniang
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
