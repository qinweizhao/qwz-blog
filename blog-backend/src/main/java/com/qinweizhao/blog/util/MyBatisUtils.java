package com.qinweizhao.blog.util;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinweizhao.blog.model.core.PageParam;
import com.qinweizhao.blog.model.core.PageResult;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author qinweizhao
 * @since 2022/8/11
 */
public class MyBatisUtils {

    private MyBatisUtils() {
    }

    /**
     * 构建分页
     *
     * @param pageParam pageParam
     * @param <T>       T
     * @return T
     */
    public static <T> Page<T> buildPage(PageParam pageParam) {
        // 页码 + 数量
        Page<T> page = new Page<>(pageParam.getPage(), pageParam.getSize());
        // 排序字段
        List<PageParam.Sort> sorts = pageParam.getSorts();
        if (!CollUtil.isEmpty(sorts)) {
            page.addOrder(sorts.stream().map(sortingField -> sortingField.isAsc() ? OrderItem.asc(sortingField.getField()) : OrderItem.desc(sortingField.getField())).collect(Collectors.toList()));
        }
        return page;
    }


    /**
     * 构建分页返回数据
     *
     * @param page page
     * @param <T>  T
     * @return T
     */
    public static <T> PageResult<T> buildSimplePageResult(Page<T> page) {
        return new PageResult<>(page.getRecords(), page.getTotal(), page.hasPrevious(), page.hasNext());
    }


    /**
     * 构建分页返回数据
     *
     * @param page page
     * @param <T>  T
     * @return T
     */
    public static <T> PageResult<T> buildPageResult(Page<T> page) {
        return new PageResult<>(page.getRecords(), page.getCurrent(), page.getSize(), page.getTotal(), page.hasPrevious(), page.hasNext());
    }

}
