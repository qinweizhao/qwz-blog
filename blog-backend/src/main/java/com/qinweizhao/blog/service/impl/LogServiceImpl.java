package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.convert.LogConvert;
import com.qinweizhao.blog.mapper.LogMapper;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.LogDTO;
import com.qinweizhao.blog.model.entity.Log;
import com.qinweizhao.blog.model.param.LogQueryParam;
import com.qinweizhao.blog.service.LogService;
import org.springframework.stereotype.Service;

/**
 * LogService implementation class
 *
 * @author ryanwang
 * @date 2019-03-14
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Override
    public PageResult<LogDTO> pageLogs(LogQueryParam param) {
        PageResult<Log> result = this.baseMapper.selectPageLogs(param);
        return LogConvert.INSTANCE.convert(result);
    }

}
