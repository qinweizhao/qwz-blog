package com.qinweizhao.blog.model.convert;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ConfigDTO;
import com.qinweizhao.blog.model.dto.ConfigSimpleDTO;
import com.qinweizhao.blog.model.entity.Config;
import com.qinweizhao.blog.model.enums.ConfigType;
import com.qinweizhao.blog.model.enums.ValueEnum;
import com.qinweizhao.blog.model.param.ConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface ConfigConvert {

    ConfigConvert INSTANCE = Mappers.getMapper(ConfigConvert.class);

    /**
     * convert
     *
     * @param config config
     * @return ConfigDTO
     */
    @Mappings({
            @Mapping(source = "configKey", target = "key"),
            @Mapping(source = "configValue", target = "value")
    })
    ConfigDTO convert(Config config);

    /**
     * convert
     *
     * @param config config
     * @return ConfigDTO
     */
    @Mappings({
            @Mapping(source = "configKey", target = "key"),
            @Mapping(source = "configValue", target = "value")
    })
    ConfigSimpleDTO convertSimpleDTO(Config config);

    /**
     * 转换分页
     *
     * @param page page
     * @return PageResult
     */
    PageResult<ConfigSimpleDTO> convert(PageResult<Config> page);


    /**
     * 状态转换
     *
     * @param type type
     * @return ConfigType
     */
    default ConfigType statusToEnum(Integer type) {
        return ValueEnum.valueToEnum(ConfigType.class, type);
    }


    /**
     * convert
     *
     * @param param param
     * @return Config
     */
    default Config convert(ConfigParam param) {
        if (param == null) {
            return null;
        }
        Config config = new Config();
        config.setConfigKey(param.getKey());
        config.setConfigValue(param.getValue());
        config.setType(param.getType().getValue());
        return config;
    }
}
