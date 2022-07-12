package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.Option;

/**
 * @author qinweizhao
 * @since 2022/7/5
 */
public interface OptionMapper extends BaseMapper<Option> {

    /**
     * 通过 key 查询
     *
     * @param key key
     * @return Option
     */
    default Option selectByKey(String key) {
        return selectOne(new LambdaQueryWrapper<Option>()
                .eq(Option::getOptionKey, key));
    }

    /**
     * 通过 key 删除
     *
     * @param key key
     * @return int
     */
    default int deleteByKey(String key) {
        return delete(new LambdaQueryWrapper<Option>()
                .eq(Option::getOptionKey, key));
    }

}
