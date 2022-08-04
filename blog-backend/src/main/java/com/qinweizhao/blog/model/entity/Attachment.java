package com.qinweizhao.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.qinweizhao.blog.model.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 附件
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Attachment extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名字
     */
    private String name;

    /**
     * 路径
     */
    private String path;

    /**
     * 文件 key :oss 文件或本地文件key 只用于删除
     */
    private String fileKey;

    /**
     * 缩略图 路径
     */
    private String thumbPath;

    /**
     * 类型
     */
    private String mediaType;

    /**
     * 后缀（png, zip, mp4, jpg）
     */
    private String suffix;

    /**
     * 宽度
     */
    private Integer width;

    /**
     * 高度
     */
    private Integer height;

    /**
     * 大小
     */
    private Long size;

    /**
     * 上传类型（本地，云）
     */
    private Integer type;

}
