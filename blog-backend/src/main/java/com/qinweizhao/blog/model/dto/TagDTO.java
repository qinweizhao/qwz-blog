package com.qinweizhao.blog.model.dto;

import lombok.Data;
import com.qinweizhao.blog.model.dto.base.OutputConverter;

import java.util.Date;

/**
 * Tag output dto.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-19
 */
@Data
public class TagDTO implements OutputConverter<TagDTO, Tag> {

    private Integer id;

    private String name;

    private String slug;

    private String thumbnail;

    private Date createTime;

    private String fullPath;
}
