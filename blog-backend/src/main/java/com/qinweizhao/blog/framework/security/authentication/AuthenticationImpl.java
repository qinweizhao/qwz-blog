package com.qinweizhao.blog.framework.security.authentication;

import com.qinweizhao.blog.framework.security.support.UserDetail;

/**
 * Authentication implementation.
 */
public class AuthenticationImpl implements Authentication {

    private final UserDetail userDetail;

    public AuthenticationImpl(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    @Override
    public UserDetail getDetail() {
        return userDetail;
    }
}
