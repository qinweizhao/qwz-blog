package com.qinweizhao.blog.framework.security.util;

import com.qinweizhao.blog.framework.security.authentication.Authentication;
import com.qinweizhao.blog.framework.security.context.SecurityContextHolder;
import com.qinweizhao.blog.framework.security.support.UserDetail;
import com.qinweizhao.blog.model.entity.User;

import java.util.Objects;

/**
 * @author qinweizhao
 * @since 2022/7/12
 */
public class SecurityUtils {

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static UserDetail getDetail() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getDetail();
    }

    public static User getLoginUser() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getDetail().getUser();
    }


    public static Integer getUserId() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getDetail().getUser().getId();
    }


}
