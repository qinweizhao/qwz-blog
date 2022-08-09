package com.qinweizhao.blog.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.exception.BadRequestException;
import com.qinweizhao.blog.exception.NotFoundException;
import com.qinweizhao.blog.exception.ServiceException;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.framework.event.logger.LogEvent;
import com.qinweizhao.blog.mail.MailService;
import com.qinweizhao.blog.model.dto.EnvironmentDTO;
import com.qinweizhao.blog.model.entity.User;
import com.qinweizhao.blog.model.enums.LogType;
import com.qinweizhao.blog.model.param.LoginParam;
import com.qinweizhao.blog.model.param.ResetPasswordParam;
import com.qinweizhao.blog.model.properties.EmailProperties;
import com.qinweizhao.blog.model.support.HaloConst;
import com.qinweizhao.blog.security.authentication.Authentication;
import com.qinweizhao.blog.security.context.SecurityContextHolder;
import com.qinweizhao.blog.security.token.AuthToken;
import com.qinweizhao.blog.security.util.AuthUtils;
import com.qinweizhao.blog.service.AdminService;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.UserService;
import com.qinweizhao.blog.util.HaloUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.qinweizhao.blog.model.support.HaloConst.DATABASE_PRODUCT_NAME;

/**
 * Admin service implementation.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-04-29
 */
@Slf4j
@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final OptionService optionService;

    private final UserService userService;

    private final MailService mailService;

    private final AbstractStringCacheStore cacheStore;

    private final MyBlogProperties myBlogProperties;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public User authenticate(LoginParam loginParam) {
        Assert.notNull(loginParam, "登录参数不能为空");

        String username = loginParam.getUsername();

        String mismatchTip = "用户名或者密码不正确";

        final User user;

        try {
            // 通过用户名或电子邮件获取用户
            user = Validator.isEmail(username) ?
                    userService.getByEmailOfNonNull(username) : userService.getByUsernameOfNonNull(username);
        } catch (NotFoundException e) {
            log.error("查找用户失败: " + username);
            eventPublisher.publishEvent(new LogEvent(this, loginParam.getUsername(), LogType.LOGIN_FAILED, loginParam.getUsername()));

            throw new BadRequestException(mismatchTip);
        }

        userService.mustNotExpire(user);

        if (!userService.passwordMatch(user, loginParam.getPassword())) {
            // 如果密码不匹配 记录日志
            eventPublisher.publishEvent(new LogEvent(this, loginParam.getUsername(), LogType.LOGIN_FAILED, loginParam.getUsername()));

            throw new BadRequestException(mismatchTip);
        }

        return user;
    }

    @Override
    public AuthToken attemptAuthentication(final LoginParam loginParam) {
        // 认证
        final User user = this.authenticate(loginParam);

        if (SecurityContextHolder.getContext().isAuthenticated()) {
            throw new BadRequestException("您已登录，请不要重复登录");
        }

        // 日志记录登录成功
        eventPublisher.publishEvent(new LogEvent(this, user.getUsername(), LogType.LOGGED_IN, user.getNickname()));

        // 生成新的 token
        return buildAuthToken(user);
    }

    @Override
    public void clearToken() {
        // 检查当前是否正在登录
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new BadRequestException("您尚未登录，因此无法注销");
        }

        // Get current user
        User user = authentication.getDetail().getUser();

        // Clear access token
        cacheStore.getAny(AuthUtils.buildAccessTokenKey(user), String.class).ifPresent(accessToken -> {
            // Delete token
            cacheStore.delete(AuthUtils.buildTokenAccessKey(accessToken));
            cacheStore.delete(AuthUtils.buildAccessTokenKey(user));
        });

        // Clear refresh token
        cacheStore.getAny(AuthUtils.buildRefreshTokenKey(user), String.class).ifPresent(refreshToken -> {
            cacheStore.delete(AuthUtils.buildTokenRefreshKey(refreshToken));
            cacheStore.delete(AuthUtils.buildRefreshTokenKey(user));
        });

        eventPublisher.publishEvent(new LogEvent(this, user.getUsername(), LogType.LOGGED_OUT, user.getNickname()));

        log.info("You have been logged out, looking forward to your next visit!");
    }

    @Override
    public void sendResetPasswordCode(ResetPasswordParam param) {
        cacheStore.getAny("code", String.class).ifPresent(code -> {
            throw new ServiceException("已经获取过验证码，不能重复获取");
        });

        if (!userService.verifyUser(param.getUsername(), param.getEmail())) {
            throw new ServiceException("用户名或者邮箱验证错误");
        }

        // Gets random code.
        String code = RandomUtil.randomNumbers(6);

        log.info("Got reset password code:{}", code);

        // Cache code.
        cacheStore.putAny("code", code, 5, TimeUnit.MINUTES);

        Boolean emailEnabled = optionService.getByPropertyOrDefault(EmailProperties.ENABLED, Boolean.class, false);

        if (!emailEnabled) {
            throw new ServiceException("未启用 SMTP 服务，无法发送邮件，但是你可以通过系统日志找到验证码");
        }

        // Send email to administrator.
        String content = "您正在进行密码重置操作，如不是本人操作，请尽快做好相应措施。密码重置验证码如下（五分钟有效）：\n" + code;
        mailService.sendTextMail(param.getEmail(), "找回密码验证码", content);
    }

    @Override
    public void resetPasswordByCode(ResetPasswordParam param) {
        if (StringUtils.isEmpty(param.getCode())) {
            throw new ServiceException("验证码不能为空");
        }

        if (StringUtils.isEmpty(param.getPassword())) {
            throw new ServiceException("密码不能为空");
        }

        if (!userService.verifyUser(param.getUsername(), param.getEmail())) {
            throw new ServiceException("用户名或者邮箱验证错误");
        }

        // verify code
        String code = cacheStore.getAny("code", String.class).orElseThrow(() -> new ServiceException("未获取过验证码"));
        if (!code.equals(param.getCode())) {
            throw new ServiceException("验证码不正确");
        }

        User user = userService.getCurrentUser().orElseThrow(() -> new ServiceException("未查询到博主信息"));

        // reset password
        userService.setPassword(user, param.getPassword());

        // Update this user
        userService.updateById(user);

        // clear code cache
        cacheStore.delete("code");
    }


    @Override
    public EnvironmentDTO getEnvironments() {
        EnvironmentDTO environmentDTO = new EnvironmentDTO();

        // Get application start time.
        environmentDTO.setStartTime(ManagementFactory.getRuntimeMXBean().getStartTime());

        environmentDTO.setDatabase(DATABASE_PRODUCT_NAME);

        environmentDTO.setVersion(HaloConst.HALO_VERSION);

        environmentDTO.setMode(myBlogProperties.getMode());

        return environmentDTO;
    }

    @Override
    public AuthToken refreshToken(String refreshToken) {
        Assert.hasText(refreshToken, "刷新令牌不能为空");

        Integer userId = cacheStore.getAny(AuthUtils.buildTokenRefreshKey(refreshToken), Integer.class)
                .orElseThrow(() -> new BadRequestException("登录状态已失效，请重新登录").setErrorData(refreshToken));

        // Get user info
        User user = userService.getById(userId);

        // Remove all token
        cacheStore.getAny(AuthUtils.buildAccessTokenKey(user), String.class)
                .ifPresent(accessToken -> cacheStore.delete(AuthUtils.buildTokenAccessKey(accessToken)));
        cacheStore.delete(AuthUtils.buildTokenRefreshKey(refreshToken));
        cacheStore.delete(AuthUtils.buildAccessTokenKey(user));
        cacheStore.delete(AuthUtils.buildRefreshTokenKey(user));

        return buildAuthToken(user);
    }

    /**
     * 构建身份验证令牌
     *
     * @param user 用户信息不能为空
     * @return 令牌
     */
    private AuthToken buildAuthToken(User user) {
        Assert.notNull(user, "用户不能为空");

        // 生成新令牌
        AuthToken token = new AuthToken();

        token.setAccessToken(HaloUtils.randomUUIDWithoutDash());
        token.setExpiredIn(ACCESS_TOKEN_EXPIRED_SECONDS);
        token.setRefreshToken(HaloUtils.randomUUIDWithoutDash());

        // 缓存这些令牌，仅用于清除
        cacheStore.putAny(AuthUtils.buildAccessTokenKey(user), token.getAccessToken(), ACCESS_TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);
        cacheStore.putAny(AuthUtils.buildRefreshTokenKey(user), token.getRefreshToken(), REFRESH_TOKEN_EXPIRED_DAYS, TimeUnit.DAYS);

        // 使用用户 ID 缓存这些令牌
        cacheStore.putAny(AuthUtils.buildTokenAccessKey(token.getAccessToken()), user.getId(), ACCESS_TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);
        cacheStore.putAny(AuthUtils.buildTokenRefreshKey(token.getRefreshToken()), user.getId(), REFRESH_TOKEN_EXPIRED_DAYS, TimeUnit.DAYS);

        return token;
    }

    @Override
    public String getLogFiles(Long lines) {
        Assert.notNull(lines, "Lines must not be null");

        File file = new File(myBlogProperties.getWorkDir(), LOG_PATH);

        List<String> linesArray = new ArrayList<>();

        StringBuilder result = new StringBuilder();

        if (!file.exists()) {
            return StringUtils.EMPTY;
        }
        long count = 0;

        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            long length = randomAccessFile.length();
            if (length == 0L) {
                return StringUtils.EMPTY;
            } else {
                long pos = length - 1;
                while (pos > 0) {
                    pos--;
                    randomAccessFile.seek(pos);
                    if (randomAccessFile.readByte() == '\n') {
                        String line = randomAccessFile.readLine();
                        linesArray.add(new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
                        count++;
                        if (count == lines) {
                            break;
                        }
                    }
                }
                if (pos == 0) {
                    randomAccessFile.seek(0);
                    linesArray.add(new String(randomAccessFile.readLine().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
                }
            }
        } catch (Exception e) {
            throw new ServiceException("读取日志失败", e);
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Collections.reverse(linesArray);

        linesArray.forEach(line -> result.append(line)
                .append(StringUtils.LF));

        return result.toString();
    }


}
