package com.qinweizhao.blog.framework.security.support;

import com.qinweizhao.blog.exception.AuthenticationException;
import com.qinweizhao.blog.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.NonNull;

/**
 * User detail.
 */
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail {

    private User user;

    /**
     * Gets user info.
     *
     * @return user info
     * @throws AuthenticationException throws if the user is null
     */
    @NonNull
    public User getUser() {
        return user;
    }

    /**
     * Sets user info.
     *
     * @param user user info
     */
    public void setUser(User user) {
        this.user = user;
    }
}
