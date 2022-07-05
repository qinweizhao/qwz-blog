package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.annotation.DisableOnCondition;
import com.qinweizhao.blog.model.dto.UserDTO;
import com.qinweizhao.blog.model.entity.User;
import com.qinweizhao.blog.model.params.PasswordParam;
import com.qinweizhao.blog.model.params.UserParam;
import com.qinweizhao.blog.model.support.BaseResponse;
import com.qinweizhao.blog.model.support.UpdateCheck;
import com.qinweizhao.blog.service.UserService;
import com.qinweizhao.blog.utils.ValidationUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * User controller.
 *
 * @author johnniang
 * @date 2019-03-19
 */
@RestController
@RequestMapping("/api/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("profiles")
    @ApiOperation("Gets user profile")
    public UserDTO getProfile(User user) {
        return new UserDTO().convertFrom(user);
    }

    @PutMapping("profiles")
    @ApiOperation("Updates user profile")
    @DisableOnCondition
    public UserDTO updateProfile(@RequestBody UserParam userParam, User user) {
        // Validate the user param
        ValidationUtils.validate(userParam, UpdateCheck.class);

        // Update properties
        userParam.update(user);

        // Update user and convert to dto
        userService.updateById(user);
        return new UserDTO().convertFrom(user);
    }

    @PutMapping("profiles/password")
    @ApiOperation("Updates user's password")
    @DisableOnCondition
    public BaseResponse<String> updatePassword(@RequestBody @Valid PasswordParam passwordParam, User user) {
        userService.updatePassword(passwordParam.getOldPassword(), passwordParam.getNewPassword(), user.getId());
        return BaseResponse.ok("密码修改成功");
    }

}
