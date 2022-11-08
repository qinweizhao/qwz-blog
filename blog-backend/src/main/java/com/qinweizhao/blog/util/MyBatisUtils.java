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
        // 示例：sort:"top_priority-desc,create_time-desc"
        String sorts = param.getSort();
        if (!ObjectUtils.isEmpty(sorts)) {

            String[] sort = sorts.split(",");
            for (String item : sort) {
                String[] split = item.split("-");
                if (split.length == 2) {
                    String column = split[0];
                    String order = split[1].toLowerCase();
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
