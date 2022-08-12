package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.dto.EnvironmentDTO;
import com.qinweizhao.blog.model.entity.User;
import com.qinweizhao.blog.model.param.LoginParam;
import com.qinweizhao.blog.model.param.ResetPasswordParam;
import com.qinweizhao.blog.security.token.AuthToken;

/**
 * Admin service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @since 2019-04-29
 */
public interface AdminService {

    /**
     * 过期秒数
     */
    int ACCESS_TOKEN_EXPIRED_SECONDS = 24 * 3600;

    int REFRESH_TOKEN_EXPIRED_DAYS = 30;

    String LOG_PATH = "logs/spring.log";

    /**
     * 验证用户名密码
     *
     * @param loginParam loginParam
     * @return User
     */
    User authenticate(LoginParam loginParam);

    /**
     * 尝试身份验证
     *
     * @param loginParam loginParam
     * @return User
     */
    AuthToken attemptAuthentication(LoginParam loginParam);

    /**
     * 清除身份验证
     */
    void clearToken();

    /**
     * 将重置密码验证码发送到管理员的电子邮件
     *
     * @param param param
     */
    void sendResetPasswordCode(ResetPasswordParam param);

    /**
     * 通过验证码重置密码
     *
     * @param param param must not be null
     */
    void resetPasswordByCode(ResetPasswordParam param);

    /**
     * 获取系统环境
     *
     * @return EnvironmentDTO
     */
    EnvironmentDTO getEnvironments();

    /**
     * 刷新令牌
     *
     * @param refreshToken refreshToken
     * @return AuthToken
     */
    AuthToken refreshToken(String refreshToken);

    /**
     * 获取日志文件
     *
     * @param lines lines
     * @return String
     */
    String getLogFiles(Long lines);

}
