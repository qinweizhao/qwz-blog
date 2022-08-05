package com.qinweizhao.blog.convert;


import com.qinweizhao.blog.model.dto.MetaDTO;
import com.qinweizhao.blog.model.entity.Meta;
import com.qinweizhao.blog.util.ServiceUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface MetaConvert {

    MetaConvert INSTANCE = Mappers.getMapper(MetaConvert.class);

    /**
     * convertToDTO
     * @param metas metas
     * @return List
     */
    List<MetaDTO> convertToDTO(List<Meta> metas);
}
