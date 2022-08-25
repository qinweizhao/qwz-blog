package com.qinweizhao.blog.model.enums;

/**
 * 日志类型
 *
 * @author qinweizhao
 */
public enum LogType implements ValueEnum<Integer> {

    /**
     * 博客初始化
     */
    BLOG_INITIALIZED(0),

    /**
     * 发表文章
     */
    POST_PUBLISHED(5),

    /**
     * 编辑文章
     */
    POST_EDITED(15),

    /**
     * 删除文章
     */
    POST_DELETED(20),

    /**
     * 登陆
     */
    LOGGED_IN(25),

    /**
     * 退出
     */
    LOGGED_OUT(30),

    /**
     * 记录失败
     */
    LOGIN_FAILED(35),

    /**
     * 更新密码
     */
    PASSWORD_UPDATED(40),

    /**
     * 更新个人资料
     */
    PROFILE_UPDATED(45);

    private final Integer value;

    LogType(Integer value) {
        this.value = value;
    }


    @Override
    public Integer getValue() {
        return value;
    }
}
