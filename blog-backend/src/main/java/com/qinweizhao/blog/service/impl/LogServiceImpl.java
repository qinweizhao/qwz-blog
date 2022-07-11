package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.mapper.LogMapper;
import com.qinweizhao.blog.model.entity.Log;
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


//    @Override
//    public Page<LogDTO> pageLatest(int top) {
//        Assert.isTrue(top > 0, "Top number must not be less than 0");
//
//        // Build page request
//        PageRequest latestPageable = PageRequest.of(0, top, Sort.by(Sort.Direction.DESC, "createTime"));
//
//        // List all
//        return listAll(latestPageable).map(log -> new LogDTO().convertFrom(log));
//    }
}
