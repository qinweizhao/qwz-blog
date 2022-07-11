package com.qinweizhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.blog.exception.ForbiddenException;
import com.qinweizhao.blog.exception.NotFoundException;
import com.qinweizhao.blog.model.entity.User;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * User service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-14
 */
public interface UserService extends IService<User> {

    /**
     * Login failure count key.
     */
    String LOGIN_FAILURE_COUNT_KEY = "login.failure.count";

    /**
     * Max login try count.
     */
    int MAX_LOGIN_TRY = 5;

    /**
     * Lock minutes.
     */
    int LOCK_MINUTES = 10;

    /**
     * Gets current user.
     *
     * @return an optional user
     */
    @NonNull
    Optional<User> getCurrentUser();

    /**
     * Gets user by username.
     *
     * @param username username must not be blank
     * @return an optional user
     */
    @NonNull
    Optional<User> getByUsername(@NonNull String username);

    /**
     * Gets non null user by username.
     *
     * @param username username
     * @return user info
     * @throws NotFoundException throws when the username does not exist
     */
    @NonNull
    User getByUsernameOfNonNull(String username);

    /**
     * Gets user by email.
     *
     * @param email email must not be blank
     * @return an optional user
     */
    @NonNull
    Optional<User> getByEmail(@NonNull String email);

    /**
     * Gets non null user by email.
     *
     * @param email email
     * @return user info
     * @throws NotFoundException throws when the username does not exist
     */
    @NonNull
    User getByEmailOfNonNull(@NonNull String email);

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
    void mustNotExpire(@NonNull User user);

    /**
     * Checks the password is match the user password.
     *
     * @param user          user info must not be null
     * @param plainPassword plain password
     * @return true if the given password is match the user password; false otherwise
     */
    boolean passwordMatch(@NonNull User user, @Nullable String plainPassword);

    /**
     * Set user password.
     *
     * @param user          user must not be null
     * @param plainPassword plain password must not be blank
     */
    void setPassword(@NonNull User user, @NonNull String plainPassword);

    /**
     * verify user's email and username
     *
     * @param username username must not be null
     * @param password password must not be null
     * @return boolean
     */
    boolean verifyUser(@NonNull String username, @NonNull String password);


}
