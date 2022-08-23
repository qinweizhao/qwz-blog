package com.qinweizhao.blog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2019-03-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostListDTO extends PostSimpleDTO {

    private List<TagDTO> tags;

    private List<MetaDTO> metas;

}
