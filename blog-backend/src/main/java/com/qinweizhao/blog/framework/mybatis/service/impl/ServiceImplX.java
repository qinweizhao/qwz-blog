package com.qinweizhao.blog.framework.mybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.framework.mybatis.mapper.BaseMapperX;
import com.qinweizhao.blog.framework.mybatis.service.IServiceX;

/**
 * @author qinweizhao
 */
public class ServiceImplX<M extends BaseMapperX<T>, T> extends ServiceImpl<M, T> implements IServiceX<T> {


}
