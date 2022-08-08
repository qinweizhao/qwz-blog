package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.framework.annotation.DisableOnCondition;
import com.qinweizhao.blog.framework.cache.lock.CacheLock;
import com.qinweizhao.blog.model.dto.EnvironmentDTO;
import com.qinweizhao.blog.model.param.LoginParam;
import com.qinweizhao.blog.model.param.ResetPasswordParam;
import com.qinweizhao.blog.security.token.AuthToken;
import com.qinweizhao.blog.service.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Admin controller.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-03-19
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * 登陆
     *
     * @param loginParam loginParam
     * @return AuthToken
     */
    @PostMapping("login")
    @CacheLock(autoDelete = false, prefix = "login_auth")
    public AuthToken auth(@RequestBody @Valid LoginParam loginParam) {
        return adminService.attemptAuthentication(loginParam);
    }

    /**
     * 退出
     */
    @PostMapping("logout")
    @CacheLock(autoDelete = false)
    public void logout() {
        adminService.clearToken();
    }


    /**
     * 重置密码
     *
     * @param param param
     */
    @PostMapping("password/code")
    @CacheLock(autoDelete = false)
    @DisableOnCondition
    public void sendResetCode(@RequestBody @Valid ResetPasswordParam param) {
        adminService.sendResetPasswordCode(param);
    }

    /**
     * 重置密码（通过验证码重置密码）
     *
     * @param param param
     */
    @PutMapping("password/reset")
    @CacheLock(autoDelete = false)
    @DisableOnCondition
    public void resetPassword(@RequestBody @Valid ResetPasswordParam param) {
        adminService.resetPasswordByCode(param);
    }

    /**
     * 刷新令牌
     *
     * @param refreshToken refreshToken
     * @return AuthToken
     */
    @PostMapping("refresh/{refreshToken}")
    @CacheLock(autoDelete = false)
    public AuthToken refresh(@PathVariable("refreshToken") String refreshToken) {
        return adminService.refreshToken(refreshToken);
    }

    /**
     * 获取环境信息
     *
     * @return EnvironmentDTO
     */
    @GetMapping("environments")
    public EnvironmentDTO getEnvironments() {
        return adminService.getEnvironments();
    }

    /**
     * 获取日志文件内容
     *
     * @param lines lines
     * @return BaseResponse
     */
    @GetMapping(value = "halo/logfile")
    @DisableOnCondition
    public String getLogFiles(@RequestParam("lines") Long lines) {
        return adminService.getLogFiles(lines);
    }
}
