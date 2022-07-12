package com.qinweizhao.blog.utils;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinweizhao.blog.model.base.PageParam;
import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.entity.Comment;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MyBatis 工具类
 *
 * @author weizhao
 */
public class MyBatisUtils {

    public static <T> Page<T> buildPage(PageParam pageParam) {
        // 页码 + 数量
        Page<T> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        // 排序字段
        List<PageParam.Sort> sorts = pageParam.getSorts();
        if (!CollectionUtil.isEmpty(sorts)) {
            page.addOrder(sorts.stream().map(sortingField -> sortingField.isAsc() ?
                            OrderItem.asc(sortingField.getField()) : OrderItem.desc(sortingField.getField()))
                    .collect(Collectors.toList()));
        }
        return page;
    }


    public static <T> PageResult<T> buildPageResult(Page<T> page) {
        return new PageResult<T>(page.getRecords(),page.getTotal());
    }
}
