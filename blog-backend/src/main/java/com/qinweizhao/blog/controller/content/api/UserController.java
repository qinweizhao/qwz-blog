package com.qinweizhao.blog.controller.content.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Content user controller.
 *
 * @author johnniang
 * @since 2019-04-03
 */
@RestController("ApiContentUserController")
@RequestMapping("/api/content/users")
public class UserController {

//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping("profile")
//    @ApiOperation("Gets blogger profile")
//    public UserDTO getProfile() {
//        return userService.getCurrentUser().map(user -> (UserDTO) new UserDTO().convertFrom(user)).get();
//    }
}
