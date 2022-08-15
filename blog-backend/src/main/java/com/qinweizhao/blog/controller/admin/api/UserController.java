package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.framework.annotation.DisableOnCondition;
import com.qinweizhao.blog.model.convert.UserConvert;
import com.qinweizhao.blog.model.dto.UserDTO;
import com.qinweizhao.blog.model.param.PasswordParam;
import com.qinweizhao.blog.model.param.UserUpdateParam;
import com.qinweizhao.blog.model.support.BaseResponse;
import com.qinweizhao.blog.security.util.SecurityUtils;
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
@RequestMapping("/api/admin/users")
public class UserController {

    private final UserService userService;

    /**
     * 获取用户资料
     *
     * @return UserDTO
     */
    @GetMapping("profiles")
    public BaseResponse<UserDTO> getProfile() {
        UserDTO userDTO = UserConvert.INSTANCE.convert(SecurityUtils.getLoginUser());
        return BaseResponse.ok(userDTO);
    }


    /**
     * 更新用户资料
     *
     * @param userParam userParam
     * @return UserDTO
     */
    @PutMapping("profiles")
    @DisableOnCondition
    public BaseResponse<UserDTO> updateProfile(@RequestBody @Validated UserUpdateParam userParam) {
        UserDTO userDTO = userService.updateProfile(userParam);
        return BaseResponse.ok(userDTO);
    }

    /**
     * 更新用户密码
     *
     * @param passwordParam passwordParam
     * @return BaseResponse
     */
    @PutMapping("profiles/password")
    @DisableOnCondition
    public BaseResponse<String> updatePassword(@RequestBody @Validated PasswordParam passwordParam) {
        userService.updatePassword(passwordParam.getOldPassword(), passwordParam.getNewPassword(), SecurityUtils.getUserId());
        return BaseResponse.ok("密码修改成功");
    }

}
