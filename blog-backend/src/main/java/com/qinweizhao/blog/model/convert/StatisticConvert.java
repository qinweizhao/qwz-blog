package com.qinweizhao.blog.model.convert;


import com.qinweizhao.blog.model.dto.StatisticDTO;
import com.qinweizhao.blog.model.dto.StatisticWithUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface StatisticConvert {

    StatisticConvert INSTANCE = Mappers.getMapper(StatisticConvert.class);

    /**
     * convert
     *
     * @param statistic statistic
     * @return StatisticWithUserDTO
     */
    StatisticWithUserDTO convert(StatisticDTO statistic);


}
