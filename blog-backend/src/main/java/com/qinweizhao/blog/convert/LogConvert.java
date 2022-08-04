package com.qinweizhao.blog.convert;


import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.LogDTO;
import com.qinweizhao.blog.model.entity.Log;
import com.qinweizhao.blog.model.enums.LogType;
import com.qinweizhao.blog.model.enums.ValueEnum;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface LogConvert {

    LogConvert INSTANCE = Mappers.getMapper(LogConvert.class);

    /**
     * convert
     *
     * @param log log
     * @return PageResult
     */
    PageResult<LogDTO> convert(PageResult<Log> log);

    /**
     * 状态转换
     *
     * @param type type
     * @return LogType
     */
    default LogType typeToEnum(Integer type) {
        return ValueEnum.valueToEnum(LogType.class, type);
    }

}
