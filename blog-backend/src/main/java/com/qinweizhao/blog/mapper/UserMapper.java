package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

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
    default Optional<User> selectByUsername(String username) {
        return Optional.of(this.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username)));
    }

    /**
     * 通过邮箱查询用户
     *
     * @param email email
     * @return User
     */
    default Optional<User> selectByByEmail(String email) {
        return Optional.of(this.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email)));
    }

}
