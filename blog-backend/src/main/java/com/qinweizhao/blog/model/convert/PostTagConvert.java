package com.qinweizhao.blog.model.convert;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface PostTagConvert {

    PostTagConvert INSTANCE = Mappers.getMapper(PostTagConvert.class);

}
