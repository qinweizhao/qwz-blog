package com.qinweizhao.blog.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.exception.BadRequestException;
import com.qinweizhao.blog.exception.ForbiddenException;
import com.qinweizhao.blog.framework.event.logger.LogEvent;
import com.qinweizhao.blog.framework.event.user.UserUpdatedEvent;
import com.qinweizhao.blog.framework.security.util.SecurityUtils;
import com.qinweizhao.blog.mapper.UserMapper;
import com.qinweizhao.blog.model.convert.UserConvert;
import com.qinweizhao.blog.model.dto.UserDTO;
import com.qinweizhao.blog.model.entity.User;
import com.qinweizhao.blog.model.enums.LogType;
import com.qinweizhao.blog.model.param.UserUpdateParam;
import com.qinweizhao.blog.service.UserService;
import com.qinweizhao.blog.util.DateUtils;
import com.qinweizhao.blog.util.HaloUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * UserService implementation class.
 *
 * @author ryanwang
 * @author johnniang
 * @since 2019-03-14
 */
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final ApplicationEventPublisher eventPublisher;


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
    public User getByUsername(String username) {
        return this.baseMapper.selectByUsername(username);
    }


    @Override
    public User getByEmail(String email) {
        return this.baseMapper.selectByByEmail(email);
    }

    @Override
    public boolean updatePassword(String oldPassword, String newPassword, Integer userId) {
        Assert.hasText(oldPassword, "旧密码不能为空");
        Assert.hasText(newPassword, "新密码不能为空");
        Assert.notNull(userId, "用户编号不能为空");

        if (oldPassword.equals(newPassword)) {
            throw new BadRequestException("新密码和旧密码不能相同");
        }

        User user = getById(userId);

        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw new BadRequestException("旧密码错误").setErrorData(oldPassword);
        }

        this.setPassword(user, newPassword);

        update(user);

        eventPublisher.publishEvent(new LogEvent(this, user.getId().toString(), LogType.PASSWORD_UPDATED, HaloUtils.desensitize(oldPassword, 2, 1)));

        return true;
    }


    @Override
    public void mustNotExpire(User user) {
        Date now = DateUtils.now();
        if (user.getExpireTime() != null && user.getExpireTime().after(now)) {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(user.getExpireTime().getTime() - now.getTime());
            throw new ForbiddenException("账号已被停用，请 " + HaloUtils.timeFormat(seconds) + " 后重试").setErrorData(seconds);
        }
    }

    @Override
    public boolean passwordMatch(User user, String plainPassword) {
        Assert.notNull(user, "用户不能为空");

        return !StringUtils.isBlank(plainPassword) && BCrypt.checkpw(plainPassword, user.getPassword());
    }


    public boolean update(User user) {
        boolean b = this.updateById(user);
        eventPublisher.publishEvent(new LogEvent(this, user.getId().toString(), LogType.PROFILE_UPDATED, user.getUsername()));
        eventPublisher.publishEvent(new UserUpdatedEvent(this, user.getId()));

        return b;
    }

    @Override
    public void setPassword(User user, String plainPassword) {
        Assert.notNull(user, "用户不能为空");
        Assert.hasText(plainPassword, "密码不能为空");
        user.setPassword(BCrypt.hashpw(plainPassword, BCrypt.gensalt()));
    }

    @Override
    public boolean verifyUser(String username, String email) {
        User user = this.getByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            return false;
        }
        return user.getUsername().equals(username) && user.getEmail().equals(email);
    }

    @Override
    public UserDTO updateProfile(UserUpdateParam userParam) {
        User user = UserConvert.INSTANCE.convert(userParam);
        user.setId(SecurityUtils.getUserId());
        this.updateById(user);
        return UserConvert.INSTANCE.convert(user);
    }

}
