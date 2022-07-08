package com.qinweizhao.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.qinweizhao.blog.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Photo extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String description;

    private String location;

    private String name;

    private LocalDateTime takeTime;

    private String team;

    private String thumbnail;

    private String url;

}
