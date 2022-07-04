package com.qinweizhao.blog.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.cache.lock.CacheLock;
import com.qinweizhao.blog.event.logger.LogEvent;
import com.qinweizhao.blog.event.user.UserUpdatedEvent;
import com.qinweizhao.blog.exception.BadRequestException;
import com.qinweizhao.blog.exception.ForbiddenException;
import com.qinweizhao.blog.exception.NotFoundException;
import com.qinweizhao.blog.exception.ServiceException;
import com.qinweizhao.blog.mapper.UserMapper;
import com.qinweizhao.blog.model.enums.LogType;
import com.qinweizhao.blog.model.params.UserParam;
import com.qinweizhao.blog.model.entity.User;
import com.qinweizhao.blog.service.UserService;
import com.qinweizhao.blog.utils.DateUtils;
import com.qinweizhao.blog.utils.HaloUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * UserService implementation class.
 *
 * @author ryanwang
 * @author johnniang
 * @date 2019-03-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {


    @Resource
    private AbstractStringCacheStore stringCacheStore;

    @Resource
    private ApplicationEventPublisher eventPublisher;


    @Override
    public Optional<User> getCurrentUser() {
        // 获取所有用户
        List<User> users = this.list();

        if (CollectionUtils.isEmpty(users)) {
            // 返回空用户
            return Optional.empty();
        }

        // 返回第一个用户
        return Optional.of(users.get(0));
    }

    @Override
    public @NotNull Optional<User> getByUsername(String username) {
        return this.baseMapper.selectByUsername(username);
    }

    @Override
    public User getByUsernameOfNonNull(String username) {
        return getByUsername(username).orElseThrow(() -> new NotFoundException("The username does not exist").setErrorData(username));
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return this.baseMapper.selectByByEmail(email);
    }

    @Override
    public User getByEmailOfNonNull(String email) {
        return getByEmail(email).orElseThrow(() -> new NotFoundException("The email does not exist").setErrorData(email));
    }

    @Override
    public boolean updatePassword(String oldPassword, String newPassword, Integer userId) {
        Assert.hasText(oldPassword, "Old password must not be blank");
        Assert.hasText(newPassword, "New password must not be blank");
        Assert.notNull(userId, "User id must not be blank");

        if (oldPassword.equals(newPassword)) {
            throw new BadRequestException("新密码和旧密码不能相同");
        }

        // Get the user
        User user = getById(userId);

        // Check the user old password
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw new BadRequestException("旧密码错误").setErrorData(oldPassword);
        }

        // Set new password
        setPassword(user, newPassword);

        // Update this user
         update(user);

        // Log it
        eventPublisher.publishEvent(new LogEvent(this, user.getId().toString(), LogType.PASSWORD_UPDATED, HaloUtils.desensitize(oldPassword, 2, 1)));

        return true;
    }



    @Override
    public void mustNotExpire(User user) {
        Assert.notNull(user, "User must not be null");

        Date now = DateUtils.now();
        if (user.getExpireTime() != null && user.getExpireTime().after(now)) {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(user.getExpireTime().getTime() - now.getTime());
            // If expired
            throw new ForbiddenException("账号已被停用，请 " + HaloUtils.timeFormat(seconds) + " 后重试").setErrorData(seconds);
        }
    }

    @Override
    public boolean passwordMatch(User user, String plainPassword) {
        Assert.notNull(user, "User must not be null");

        return !StringUtils.isBlank(plainPassword) && BCrypt.checkpw(plainPassword, user.getPassword());
    }


    public boolean update(User user) {
        boolean b = this.updateById(user);
        // Log it
        eventPublisher.publishEvent(new LogEvent(this, user.getId().toString(), LogType.PROFILE_UPDATED, user.getUsername()));
        eventPublisher.publishEvent(new UserUpdatedEvent(this, user.getId()));

        return b;
    }

    @Override
    public void setPassword(@NonNull User user, @NonNull String plainPassword) {
        Assert.notNull(user, "User must not be null");
        Assert.hasText(plainPassword, "Plain password must not be blank");
        user.setPassword(BCrypt.hashpw(plainPassword, BCrypt.gensalt()));
    }

    @Override
    public boolean verifyUser(String username, String password) {
        User user = getCurrentUser().orElseThrow(() -> new ServiceException("未查询到博主信息"));
        return user.getUsername().equals(username) && user.getEmail().equals(password);
    }

}
