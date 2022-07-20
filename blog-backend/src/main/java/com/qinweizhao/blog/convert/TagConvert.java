package com.qinweizhao.blog.convert;


import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.dto.TagWithPostCountDTO;
import com.qinweizhao.blog.model.entity.Tag;
import com.qinweizhao.blog.model.params.TagParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface TagConvert {

    TagConvert INSTANCE = Mappers.getMapper(TagConvert.class);

    /**
     * convert
     *
     * @param tag tag
     * @return TagDTO
     */
    TagDTO convert(Tag tag);

    /**
     * convertWithPostCountDTO
     *
     * @param tag tag
     * @return TagWithPostCountDTO
     */
    TagWithPostCountDTO convertWithPostCountDTO(Tag tag);

    /**
     * convertToDTO
     *
     * @param tags tags
     * @return List
     */
    List<TagDTO> convertToDTO(List<Tag> tags);

    /**
     * convert
     *
     * @param tagParam tagParam
     * @return Tag
     */
    Tag convert(TagParam tagParam);

}
