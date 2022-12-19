package com.qinweizhao.blog.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * Attach origin.
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
public enum AttachmentType implements ValueEnum<Integer> {

    /**
     * 服务器
     */
    LOCAL(0),

    /**
     * 又拍云
     */
    UPOSS(1),

    /**
     * 七牛云
     */
    QINIUOSS(2),

    /**
     * 阿里云
     */
    ALIOSS(4),

    /**
     * 百度云
     */
    BAIDUBOS(5),

    /**
     * 腾讯云
     */
    TENCENTCOS(6),

    /**
     * 华为云
     */
    HUAWEIOBS(7),

    /**
     * MINIO
     */
    MINIO(8);

    @EnumValue
    private final Integer value;

    AttachmentType(Integer value) {
        this.value = value;
    }

    /**
     * Get enum value.
     *
     * @return enum value
     */
    @Override
    public Integer getValue() {
        return value;
    }
}
