package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.entity.Config;
import com.qinweizhao.blog.model.param.ConfigQueryParam;
import com.qinweizhao.blog.util.LambdaQueryWrapperX;
import com.qinweizhao.blog.util.MyBatisUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2022/7/5
 */
@Mapper
public interface ConfigMapper extends BaseMapper<Config> {

    Log log = LogFactory.getLog(ConfigMapper.class);

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

    /**
     * 批量更新
     *
     * @param options options
     * @return boolean
     */
    @SuppressWarnings("all")
    default boolean updateBatchById(List<Config> options) {
        String sqlStatement = SqlHelper.getSqlStatement(ConfigMapper.class, SqlMethod.UPDATE_BY_ID);
        return SqlHelper.executeBatch(Config.class, log, options, 1000, (sqlSession, entity) -> {
            MapperMethod.ParamMap param = new MapperMethod.ParamMap();
            param.put("et", entity);
            int update = sqlSession.update(sqlStatement, param);
        });

    }

    /**
     * 批量新增
     *
     * @param options options
     * @return int
     */
    default boolean insertBatch(List<Config> options) {
        String sqlStatement = SqlHelper.getSqlStatement(ConfigMapper.class, SqlMethod.INSERT_ONE);
        return SqlHelper.executeBatch(Config.class, log, options, 1000, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

}
