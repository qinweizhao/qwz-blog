package com.qinweizhao.blog.utils;

/**
 * @author qinweizhao
 * @since 2022/7/9
 */
public class ResultUtils {


    /**
     * 判断是否成功，成功返回实体，失败返回 null
     *
     * @param i      i
     * @param object object
     * @param <T>    T
     * @return T
     */
    public static <T> T judge(int i, T object) {
        return i > 0 ? object : null;
    }


    /**
     * 判断是否成功，成功返回实体，失败返回 null
     *
     * @param b      b
     * @param object object
     * @param <T>    T
     * @return T
     */
    public static <T> T judge(boolean b, T object) {
        return b ? object : null;
    }
}
