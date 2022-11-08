package com.qinweizhao.blog.util;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinweizhao.blog.model.core.PageParam;
import com.qinweizhao.blog.model.core.PageResult;
import org.springframework.util.ObjectUtils;


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
     * @param param param
     * @param <T>   T
     * @return T
     */
    public static <T> Page<T> buildPage(PageParam param) {
        // 页码 + 数量
        Page<T> page = new Page<>(param.getPage(), param.getSize());
        // 排序字段
        String sort = param.getSort();
        if (sort != null) {
            String[] split = sort.split("-");
            if (!ObjectUtils.isEmpty(split) && split.length == 2) {
                String[] columns = split[0].split(",");
                String order = split[1].toLowerCase();
                for (String column:columns){
                    OrderItem orderItem = order.equals("asc") ? OrderItem.asc(column) : OrderItem.desc(column);
                    page.addOrder(orderItem);
                }

            }
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
