package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.entity.Log;
import com.qinweizhao.blog.model.param.LogQueryParam;
import com.qinweizhao.blog.util.MyBatisUtils;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
public interface LogMapper extends BaseMapper<Log> {


    /**
     * 查询分页
     *
     * @param param param
     * @return PageResult
     */
    default PageResult<Log> selectPageLogs(LogQueryParam param) {
        Page<Log> page = MyBatisUtils.buildPage(param);
        Page<Log> logPage = this.selectPage(page, new LambdaQueryWrapper<>());
        return MyBatisUtils.buildPageResult(logPage);
    }


}
