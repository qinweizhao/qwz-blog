package com.qinweizhao.blog.util;

import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author qinweizhao
 * @since 2022-03-15
 */
public class EnumUtils {
    /**
     * 值映射为枚举
     *
     * @param enumClass 枚举类
     * @param value     枚举值
     * @param method    取值方法
     * @param <E>       对应枚举
     * @return E
     */
    public static <E extends Enum<?>> E valueOf(Class<E> enumClass, Object value, Method method) {
        E[] es = enumClass.getEnumConstants();
        for (E e : es) {
            Object eValue;
            try {
                method.setAccessible(true);
                eValue = method.invoke(e);
            } catch (IllegalAccessException | InvocationTargetException e1) {
                throw ExceptionUtils.mpe("Error: NoSuchMethod in {}.  Cause:", e, enumClass.getName());
            }
            if (value instanceof Number && eValue instanceof Number
                    && new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(String.valueOf(eValue))) == 0) {
                return e;
            }
            if (Objects.equals(eValue, value)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 根据value值获取enum对象
     *
     * @param enumClass enumClass
     * @param value     value
     * @param <E>       E
     * @return E
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
     * @param enumClass enumClass
     * @param value     value
     * @param <E>       E
     * @return E
     */
    public static <E extends Enum<E>> String getTextByValue(final Class<E> enumClass, Object value) {
        E e = getEnumByValue(enumClass, value);
        Object eValue;
        Method method;
        try {
            method = enumClass.getMethod("getText");
            method.setAccessible(true);
            eValue = method.invoke(e);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
            throw ExceptionUtils.mpe("Error: NoSuchMethod in {}.  Cause:", e, enumClass.getName());
        }
        if (eValue != null) {
            return String.valueOf(eValue);
        }
        return null;
    }

    /**
     * 根据code值获取enum对象，如果code值相同，则获取第一个enum对象
     *
     * @param enumClass enumClass
     * @param value     value
     * @param <E>       E
     * @return E
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
     * @param enumClass enumClass
     * @param value     value
     * @param <E>       E
     * @return E
     */
    public static <E extends Enum<E>> String getTextByCode(final Class<E> enumClass, Object value) {
        E e = getEnumByCode(enumClass, value);
        Object eValue;
        Method method;
        try {
            method = enumClass.getMethod("getText");
            method.setAccessible(true);
            eValue = method.invoke(e);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
            throw ExceptionUtils.mpe("Error: NoSuchMethod in {}.  Cause:", e, enumClass.getName());
        }
        if (eValue != null) {
            return String.valueOf(eValue);
        }
        return null;
    }

    /**
     * 根据code值获取enum对象
     *
     * @param enumClass enumClass
     * @param value     value
     * @param <E>       E
     * @return E
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
     * @param enumClass enumClass
     * @param value     value
     * @param <E>       E
     * @return E
     */
    public static <E extends Enum<E>> String getSubTextBySubCode(final Class<E> enumClass, Object value) {
        E e = getEnumBySubCode(enumClass, value);
        Object eValue;
        Method method;
        try {
            method = enumClass.getMethod("getSubText");
            method.setAccessible(true);
            eValue = method.invoke(e);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
            throw ExceptionUtils.mpe("Error: NoSuchMethod in {}.  Cause:", e, enumClass.getName());
        }
        if (eValue != null) {
            return String.valueOf(eValue);
        }
        return null;
    }

}
