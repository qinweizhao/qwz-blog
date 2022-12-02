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
 *
 * @author qinweizhao
 * @since 2019-03-19
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
