package com.qinweizhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.blog.exception.ForbiddenException;
import com.qinweizhao.blog.model.dto.UserDTO;
import com.qinweizhao.blog.model.entity.User;
import com.qinweizhao.blog.model.param.UserParam;
import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * User service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @since 2019-03-14
 */
public interface UserService extends IService<User> {

    /**
     * Gets current user.
     *
     * @return an optional user
     */
    Optional<User> getCurrentUser();

    /**
     * Gets user by username.
     *
     * @param username username must not be blank
     * @return an optional user
     */
    User getByUsername(String username);

    /**
     * Gets user by email.
     *
     * @param email email must not be blank
     * @return an optional user
     */

    User getByEmail(String email);

    /**
     * Updates user password.
     *
     * @param oldPassword old password must not be blank
     * @param newPassword new password must not be blank
     * @param userId      user id must not be null
     * @return updated user detail
     */
    boolean updatePassword(String oldPassword, String newPassword, Integer userId);

    /**
     * The user must not expire.
     *
     * @param user user info must not be null
     * @throws ForbiddenException throws if the given user has been expired
     */
    void mustNotExpire(User user);

    /**
     * Checks the password is match the user password.
     *
     * @param user          user info must not be null
     * @param plainPassword plain password
     * @return true if the given password is match the user password; false otherwise
     */
    boolean passwordMatch(User user, @Nullable String plainPassword);

    /**
     * Set user password.
     *
     * @param user          user must not be null
     * @param plainPassword plain password must not be blank
     */
    void setPassword(User user, String plainPassword);

    /**
     * 校验用户名和邮箱
     *
     * @param username username
     * @param email    email
     * @return boolean
     */
    boolean verifyUser(String username, String email);

    /**
     * 更新用户资料
     *
     * @param userParam userParam
     * @return UserDTO
     */
    UserDTO updateProfile(UserParam userParam);
}
