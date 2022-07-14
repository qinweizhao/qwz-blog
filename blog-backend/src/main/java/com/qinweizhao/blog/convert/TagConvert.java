package com.qinweizhao.blog.convert;


import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

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


}
