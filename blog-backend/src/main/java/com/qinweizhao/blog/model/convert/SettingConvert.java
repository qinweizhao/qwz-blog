package com.qinweizhao.blog.model.convert;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ConfigDTO;
import com.qinweizhao.blog.model.entity.Setting;
import com.qinweizhao.blog.model.param.SettingParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface SettingConvert {

    SettingConvert INSTANCE = Mappers.getMapper(SettingConvert.class);

    /**
     * convert
     *
     * @param setting config
     * @return ConfigDTO
     */
    @Mappings({
            @Mapping(source = "settingKey", target = "key"),
            @Mapping(source = "settingValue", target = "value")
    })
    ConfigDTO convert(Setting setting);

    /**
     * 转换分页
     *
     * @param page page
     * @return PageResult
     */
    PageResult<ConfigDTO> convert(PageResult<Setting> page);


    /**
     * convert
     *
     * @param param param
     * @return Config
     */
    default Setting convert(SettingParam param) {
        if (param == null) {
            return null;
        }
        Setting setting = new Setting();
        setting.setId(param.getId());
        setting.setSettingKey(param.getKey());
        setting.setSettingValue(param.getValue());
        return setting;
    }

}
