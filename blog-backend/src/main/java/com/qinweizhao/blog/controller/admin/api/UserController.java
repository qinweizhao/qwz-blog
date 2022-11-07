package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.framework.annotation.DisableOnCondition;
import com.qinweizhao.blog.framework.security.util.SecurityUtils;
import com.qinweizhao.blog.model.convert.UserConvert;
import com.qinweizhao.blog.model.dto.UserDTO;
import com.qinweizhao.blog.model.param.PasswordParam;
import com.qinweizhao.blog.model.param.UserParam;
import com.qinweizhao.blog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理
 *
 * @author qinweizhao
 * @since 2022/7/31
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/user")
public class UserController {

    private final UserService userService;

    /**
     * 获取用户资料
     *
     * @return UserDTO
     */
    @GetMapping("profiles")
    public UserDTO getProfile() {
        return UserConvert.INSTANCE.convert(SecurityUtils.getLoginUser());
    }

    /**
     * 更新用户资料
     *
     * @param userParam userParam
     * @return UserDTO
     */
    @PutMapping("profiles")
    @DisableOnCondition
    public UserDTO updateProfile(@RequestBody @Validated UserParam userParam) {
        return userService.updateProfile(userParam);
    }

    /**
     * 更新用户密码
     *
     * @param param param
     * @return BaseResponse
     */
    @PutMapping("profiles/password")
    @DisableOnCondition
    public Boolean updatePassword(@RequestBody PasswordParam param) {
        return userService.updatePassword(param.getOldPassword(), param.getNewPassword(), SecurityUtils.getUserId());
    }

}
