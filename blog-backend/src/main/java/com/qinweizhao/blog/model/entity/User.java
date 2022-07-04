package com.qinweizhao.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qinweizhao.blog.model.entity.BaseEntity;
import com.qinweizhao.blog.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * User entity
 *
 * @author ryanwang
 * @date 2019-03-12
 */
@Data
@TableName("user")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {


    /**
     * 编号
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 描述
     */
    private String description;

    /**
     * 过期时间
     */
    private Date expireTime;

}
