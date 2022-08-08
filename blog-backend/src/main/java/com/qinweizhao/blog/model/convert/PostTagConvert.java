package com.qinweizhao.blog.model.convert;


import com.qinweizhao.blog.model.dto.TagWithPostCountDTO;
import com.qinweizhao.blog.model.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface PostTagConvert {

    PostTagConvert INSTANCE = Mappers.getMapper(PostTagConvert.class);


    /**
     * convertWithPostCountDTO
     *
     * @param tag tag
     * @return TagWithPostCountDTO
     */
    TagWithPostCountDTO convert(Tag tag);


}
