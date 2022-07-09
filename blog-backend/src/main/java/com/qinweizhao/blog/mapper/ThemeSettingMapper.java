package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.ThemeSetting;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2022/7/7
 */
@Mapper
public interface ThemeSettingMapper extends BaseMapper<ThemeSetting> {

    /**
     * 按主题 ID 查找所有主题设置
     *
     * @param themeId themeId
     * @return List
     */
    default List<ThemeSetting> selectListByThemeId(String themeId) {
        return selectList(new LambdaQueryWrapper<ThemeSetting>()
                .eq(ThemeSetting::getThemeId, themeId));
    }

    /**
     * 通过主题 ID 和设置键删除主题设置
     *
     * @param themeId themeId
     * @param key     key
     * @return long
     */
    default long deleteByThemeIdAndKey(String themeId, String key) {
        return delete(new LambdaQueryWrapper<ThemeSetting>()
                .eq(ThemeSetting::getThemeId, themeId)
                .eq(ThemeSetting::getSettingKey, key));
    }

    /**
     * 通过主题 ID 和设置键查询主题设置
     *
     * @param themeId themeId
     * @param key     key
     * @return ThemeSetting
     */

    default ThemeSetting selectByThemeIdAndKey(String themeId, String key) {
        return selectOne(new LambdaQueryWrapper<ThemeSetting>()
                .eq(ThemeSetting::getThemeId, themeId)
                .eq(ThemeSetting::getSettingKey, key));
    }


    /**
     * 删除未激活的主题设置
     *
     * @param activatedThemeId 激活的主题 ID
     * @return long
     */
    default long deleteByThemeIdIsNot(String activatedThemeId) {
        return delete(new LambdaQueryWrapper<ThemeSetting>()
                .eq(ThemeSetting::getThemeId, activatedThemeId));
    }

    /**
     * 删除未激活的主题设置
     *
     * @param themeId themeId
     * @return long
     */
    default long deleteByThemeId(String themeId) {
        return delete(new LambdaQueryWrapper<ThemeSetting>()
                .eq(ThemeSetting::getThemeId, themeId));
    }
}
