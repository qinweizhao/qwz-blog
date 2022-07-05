package com.qinweizhao.blog.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.stream.Collectors;

/**
 * MyBatis 工具类
 *
 * @author weizhao
 */
public class MyBatisUtils {


    public static <T> IPage<T> buildPage(Pageable pageable) {
        // 页码 + 数量
        Page<T> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        // 排序字段
        Sort sorts = pageable.getSort();
        if (!CollectionUtil.isEmpty(sorts)) {
            page.addOrder(sorts.stream().map(sortingField -> sortingField.isAscending() ?
                            OrderItem.asc(sortingField.getProperty()) : OrderItem.desc(sortingField.getProperty()))
                    .collect(Collectors.toList()));
        }
        return page;
    }


}
