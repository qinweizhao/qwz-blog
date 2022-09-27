package com.qinweizhao.blog.model.convert;


import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.LogDTO;
import com.qinweizhao.blog.model.entity.Log;
import com.qinweizhao.blog.model.enums.LogType;
import com.qinweizhao.blog.model.enums.ValueEnum;
import com.qinweizhao.blog.model.param.LogParam;
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
     * convert
     *
     * @param type type
     * @return LogType
     */
    default LogType typeToEnum(Integer type) {
        return ValueEnum.valueToEnum(LogType.class, type);
    }

    /**
     * statusToInteger
     *
     * @param type type
     * @return Integer
     */
    default Integer typeToInteger(LogType type) {
        if (type == null) {
            return null;
        }
        Integer logType;
        switch (type) {
            case POST_PUBLISHED:
                logType = 5;
                break;
            case POST_EDITED:
                logType = 15;
                break;
            case LOGGED_IN:
                logType = 20;
                break;
            case LOGGED_OUT:
                logType = 25;
                break;
            case LOGIN_FAILED:
                logType = 30;
                break;
            case PASSWORD_UPDATED:
                logType = 35;
                break;
            case PROFILE_UPDATED:
                logType = 40;
                break;
            default:
                logType = null;
        }
        return logType;
    }

    /**
     * convert
     *
     * @param param param
     * @return Log
     */
    Log convert(LogParam param);
}
