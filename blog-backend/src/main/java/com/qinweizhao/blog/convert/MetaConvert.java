package com.qinweizhao.blog.convert;


import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.entity.Meta;
import com.qinweizhao.blog.model.entity.Tag;
import com.qinweizhao.blog.utils.ServiceUtils;
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
     * convert
     *
     * @param metas metas
     * @return convertToMap
     */
   default Map<String, Object> convertToMap(List<Meta> metas){
       return ServiceUtils.convertToMap(metas, Meta::getMetaKey, Meta::getMetaValue);
   }


}
