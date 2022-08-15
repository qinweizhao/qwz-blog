package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.entity.Config;
import com.qinweizhao.blog.model.param.ConfigQueryParam;
import com.qinweizhao.blog.util.LambdaQueryWrapperX;
import com.qinweizhao.blog.util.MyBatisUtils;

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

    /**
     * 查询分页信息
     *
     * @param param param
     * @return PageResult
     */
    default PageResult<Config> selectPage(ConfigQueryParam param) {
        Page<Config> page = MyBatisUtils.buildPage(param);
        this.selectPage(page, new LambdaQueryWrapperX<Config>()
                .likeIfPresent(Config::getOptionKey, param.getKeyword())
                .eqIfPresent(Config::getType, param.getType())
                .orderByDesc(Config::getCreateTime)
        );
        return MyBatisUtils.buildSimplePageResult(page);
    }
}
