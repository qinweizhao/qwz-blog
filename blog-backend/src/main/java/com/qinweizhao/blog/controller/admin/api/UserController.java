//package com.qinweizhao.blog.controller.admin.api;
//
//import com.qinweizhao.blog.annotation.DisableOnCondition;
//import com.qinweizhao.blog.model.dto.UserDTO;
//import com.qinweizhao.blog.model.entity.User;
//import com.qinweizhao.blog.model.params.PasswordParam;
//import com.qinweizhao.blog.model.params.UserParam;
//import com.qinweizhao.blog.model.support.BaseResponse;
//import com.qinweizhao.blog.model.support.UpdateCheck;
//import com.qinweizhao.blog.service.UserService;
//import com.qinweizhao.blog.utils.ValidationUtils;
//import lombok.AllArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
///**
// * User controller.
// *
// * @author johnniang
// * @author qinweizhao
// * @date 2019-03-19
// */
//@RestController
//@AllArgsConstructor
//@RequestMapping("/api/admin/users")
//public class UserController {
//
//    private final UserService userService;
//
//    /**
//     * 获取用户资料
//     *
//     * @param user user
//     * @return UserDTO
//     */
//    @GetMapping("profiles")
//    public UserDTO getProfile(User user) {
//        return new UserDTO().convertFrom(user);
//    }
//
//
//    /**
//     * 更新用户资料
//     *
//     * @param userParam userParam
//     * @param user      user
//     * @return UserDTO
//     */
//    @PutMapping("profiles")
//    @DisableOnCondition
//    public UserDTO updateProfile(@RequestBody UserParam userParam, User user) {
//        // 验证用户参数
//        ValidationUtils.validate(userParam, UpdateCheck.class);
//
//        // 更新属性
//        userParam.update(user);
//
//        // 更新用户并转换为 dto
//        userService.updateById(user);
//        return new UserDTO().convertFrom(user);
//    }
//
//    /**
//     * 更新用户密码
//     *
//     * @param passwordParam passwordParam
//     * @param user          user
//     * @return BaseResponse
//     */
//    @PutMapping("profiles/password")
//    @DisableOnCondition
//    public BaseResponse<String> updatePassword(@RequestBody @Valid PasswordParam passwordParam, User user) {
//        userService.updatePassword(passwordParam.getOldPassword(), passwordParam.getNewPassword(), user.getId());
//        return BaseResponse.ok("密码修改成功");
//    }
//
//}
