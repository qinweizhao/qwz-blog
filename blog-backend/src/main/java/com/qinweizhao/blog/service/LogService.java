package com.qinweizhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.LogDTO;
import com.qinweizhao.blog.model.entity.Log;
import com.qinweizhao.blog.model.params.LogQueryParam;

/**
 * Log service interface.
 *
 * @author johnniang
 * @date 2019-03-14
 */
public interface LogService extends IService<Log> {

    /**
     * 分页
     * @param param param
     * @return Page
     */
    PageResult<LogDTO> pageLogs(LogQueryParam param);


}
