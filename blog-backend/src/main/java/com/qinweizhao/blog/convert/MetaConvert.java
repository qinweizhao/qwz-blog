package com.qinweizhao.blog.convert;


import com.qinweizhao.blog.model.dto.MetaDTO;
import com.qinweizhao.blog.model.entity.Meta;
import com.qinweizhao.blog.model.param.MetaParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface MetaConvert {

    MetaConvert INSTANCE = Mappers.getMapper(MetaConvert.class);

    /**
     * convertToDTO
     *
     * @param metas metas
     * @return List
     */
    List<MetaDTO> convertToDTO(List<Meta> metas);

    /**
     * convert
     *
     * @param metas metas
     * @return Set
     */
    Set<Meta> convert(Set<MetaParam> metas);
}
