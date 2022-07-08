package com.qinweizhao.blog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.qinweizhao.blog.model.dto.base.OutputConverter;

import java.util.Date;

/**
 * Base meta Dto.
 *
 * @author ryanwang
 * @date 2019-12-10
 */
@Data
@ToString
@EqualsAndHashCode
public class BaseMetaDTO implements OutputConverter<BaseMetaDTO, BaseMeta> {
    private Long id;

    private Integer postId;

    private String key;

    private String value;

    private Date createTime;
}
