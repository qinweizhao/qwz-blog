package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author qinweizhao
 * @since 2022/7/4
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 通过用户名查询用户
     *
     * @param username username
     * @return User
     */
    default User selectByUsername(String username) {
        return this.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    /**
     * 通过邮箱查询用户
     *
     * @param email email
     * @return User
     */
    default User selectByByEmail(String email) {
        return this.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
    }

}
