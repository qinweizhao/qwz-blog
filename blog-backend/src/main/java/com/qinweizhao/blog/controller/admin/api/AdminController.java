package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.annotation.DisableOnCondition;
import com.qinweizhao.blog.cache.lock.CacheLock;
import com.qinweizhao.blog.model.dto.EnvironmentDTO;
import com.qinweizhao.blog.model.dto.StatisticDTO;
import com.qinweizhao.blog.model.params.LoginParam;
import com.qinweizhao.blog.model.params.ResetPasswordParam;
import com.qinweizhao.blog.model.support.BaseResponse;
import com.qinweizhao.blog.security.token.AuthToken;
import com.qinweizhao.blog.service.AdminService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @PostMapping("login")
    @ApiOperation("Login")
    @CacheLock(autoDelete = false, prefix = "login_auth")
    public AuthToken auth(@RequestBody @Valid LoginParam loginParam) {
        return adminService.authCodeCheck(loginParam);
    }

    @PostMapping("logout")
    @ApiOperation("Logs out (Clear session)")
    @CacheLock(autoDelete = false)
    public void logout() {
        adminService.clearToken();
    }

    @PostMapping("password/code")
    @ApiOperation("Sends reset password verify code")
    @CacheLock(autoDelete = false)
    @DisableOnCondition
    public void sendResetCode(@RequestBody @Valid ResetPasswordParam param) {
        adminService.sendResetPasswordCode(param);
    }

    @PutMapping("password/reset")
    @ApiOperation("Resets password by verify code")
    @CacheLock(autoDelete = false)
    @DisableOnCondition
    public void resetPassword(@RequestBody @Valid ResetPasswordParam param) {
        adminService.resetPasswordByCode(param);
    }

    @PostMapping("refresh/{refreshToken}")
    @ApiOperation("Refreshes token")
    @CacheLock(autoDelete = false)
    public AuthToken refresh(@PathVariable("refreshToken") String refreshToken) {
        return adminService.refreshToken(refreshToken);
    }

    @GetMapping("counts")
    @ApiOperation("获取计数信息")
    @Deprecated
    public StatisticDTO getCount() {
        return adminService.getCount();
    }

    @GetMapping("environments")
    @ApiOperation("获取环境信息")
    public EnvironmentDTO getEnvironments() {
        return adminService.getEnvironments();
    }

    @PutMapping("halo-admin")
    @ApiOperation("Updates halo-admin manually")
    @Deprecated
    public void updateAdmin() {
        adminService.updateAdminAssets();
    }

    @GetMapping(value = "halo/logfile")
    @ApiOperation("Gets halo log file content")
    @DisableOnCondition
    public BaseResponse<String> getLogFiles(@RequestParam("lines") Long lines) {
        return BaseResponse.ok(HttpStatus.OK.getReasonPhrase(), adminService.getLogFiles(lines));
    }
}
