package com.qinweizhao.blog.model.convert;


import com.qinweizhao.blog.model.dto.MetaDTO;
import com.qinweizhao.blog.model.entity.Meta;
import com.qinweizhao.blog.model.param.MetaParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
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
     * convert
     *
     * @param meta meta
     * @return MetaDTO
     */
    @Mappings({
            @Mapping(source = "metaKey", target = "key"),
            @Mapping(source = "metaValue", target = "value")
    })
    MetaDTO convert(Meta meta);


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
     * @param param param
     * @return Meta
     */
    @Mappings({
            @Mapping(source = "key", target = "metaKey"),
            @Mapping(source = "value", target = "metaValue")
    })
    Meta convert(MetaParam param);

    /**
     * convert
     *
     * @param metas metas
     * @return Set
     */
    Set<Meta> convert(Set<MetaParam> metas);
}
