package com.qinweizhao.blog.util;

import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author qinweizhao
 * @since 2022/7/18
 */
public class EnumUtils {
    /**
     * 值映射为枚举
     *
     * @param enumClass 枚举类
     * @param value     枚举值
     * @param method    取值方法
     * @param <E>       对应枚举
     * @return
     */
    public static <E extends Enum<?>> E valueOf(Class<E> enumClass, Object value, Method method) {
        E[] es = enumClass.getEnumConstants();
        for (E e : es) {
            Object evalue;
            try {
                method.setAccessible(true);
                evalue = method.invoke(e);
            } catch (IllegalAccessException | InvocationTargetException e1) {
                throw ExceptionUtils.mpe("Error: NoSuchMethod in {}.  Cause:", e, enumClass.getName());
            }
            if (value instanceof Number && evalue instanceof Number
                    && new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(String.valueOf(evalue))) == 0) {
                return e;
            }
            if (Objects.equals(evalue, value)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 根据value值获取enum对象
     *
     * @param enumClass
     * @param value
     * @param <E>
     * @return
     */
    public static <E extends Enum<E>> E getEnumByValue(final Class<E> enumClass, Object value) {
        try {
            return valueOf(enumClass, value, enumClass.getMethod("getValue"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据value值获取text
     *
     * @param enumClass
     * @param value
     * @param <E>
     * @return
     */
    public static <E extends Enum<E>> String getTextByValue(final Class<E> enumClass, Object value) {
        E e = getEnumByValue(enumClass, value);
        Object evalue;
        Method method = null;
        try {
            method = enumClass.getMethod("getText");
            method.setAccessible(true);
            evalue = method.invoke(e);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
            throw ExceptionUtils.mpe("Error: NoSuchMethod in {}.  Cause:", e, enumClass.getName());
        }
        if (evalue != null) {
            return String.valueOf(evalue);
        }
        return null;
    }

    /**
     * 根据code值获取enum对象，如果code值相同，则获取第一个enum对象
     *
     * @param enumClass
     * @param value
     * @param <E>
     * @return
     */
    public static <E extends Enum<E>> E getEnumByCode(final Class<E> enumClass, Object value) {
        try {
            return valueOf(enumClass, value, enumClass.getMethod("getCode"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据code值获取text，如果code值相同，则获取第一个enum对象的text
     *
     * @param enumClass
     * @param value
     * @param <E>
     * @return
     */
    public static <E extends Enum<E>> String getTextByCode(final Class<E> enumClass, Object value) {
        E e = getEnumByCode(enumClass, value);
        Object evalue;
        Method method = null;
        try {
            method = enumClass.getMethod("getText");
            method.setAccessible(true);
            evalue = method.invoke(e);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
            throw ExceptionUtils.mpe("Error: NoSuchMethod in {}.  Cause:", e, enumClass.getName());
        }
        if (evalue != null) {
            return String.valueOf(evalue);
        }
        return null;
    }

    /**
     * 根据code值获取enum对象
     *
     * @param enumClass
     * @param value
     * @param <E>
     * @return
     */
    public static <E extends Enum<E>> E getEnumBySubCode(final Class<E> enumClass, Object value) {
        try {
            return valueOf(enumClass, value, enumClass.getMethod("getSubCode"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据code值获取text，如果code值相同，则获取第一个enum对象的text
     *
     * @param enumClass
     * @param value
     * @param <E>
     * @return
     */
    public static <E extends Enum<E>> String getSubTextBySubCode(final Class<E> enumClass, Object value) {
        E e = getEnumBySubCode(enumClass, value);
        Object evalue;
        Method method = null;
        try {
            method = enumClass.getMethod("getSubText");
            method.setAccessible(true);
            evalue = method.invoke(e);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
            throw ExceptionUtils.mpe("Error: NoSuchMethod in {}.  Cause:", e, enumClass.getName());
        }
        if (evalue != null) {
            return String.valueOf(evalue);
        }
        return null;
    }

}
