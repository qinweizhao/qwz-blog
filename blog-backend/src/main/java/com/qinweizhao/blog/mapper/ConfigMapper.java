package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.Config;

/**
 * @author qinweizhao
 * @since 2022/7/5
 */
public interface ConfigMapper extends BaseMapper<Config> {

    /**
     * 通过 key 查询
     *
     * @param key key
     * @return Option
     */
    default Config selectByKey(String key) {
        return selectOne(new LambdaQueryWrapper<Config>()
                .eq(Config::getOptionKey, key));
    }

    /**
     * 通过 key 删除
     *
     * @param key key
     * @return int
     */
    default int deleteByKey(String key) {
        return delete(new LambdaQueryWrapper<Config>()
                .eq(Config::getOptionKey, key));
    }

}
