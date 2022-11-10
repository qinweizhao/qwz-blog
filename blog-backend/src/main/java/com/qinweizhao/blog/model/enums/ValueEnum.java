package com.qinweizhao.blog.model.enums;

import org.springframework.util.Assert;

import java.util.stream.Stream;

/**
 * Interface for value enum.
 *
 * @param <T> value type
 */
public interface ValueEnum<T> {

    /**
     * 将值转换为相应的枚举
     *
     * @param enumType enum type
     * @param value    database value
     * @param <V>      value generic
     * @param <E>      enum generic
     * @return corresponding enum
     */
    static <V, E extends ValueEnum<V>> E valueToEnum(Class<E> enumType, V value) {
        Assert.notNull(enumType, "枚举类型不能为空");
        Assert.notNull(value, "值不能为空");
        Assert.isTrue(enumType.isEnum(), "类型必须是枚举类型");

        return Stream.of(enumType.getEnumConstants())
                .filter(item -> item.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("unknown database value: " + value));
    }

    /**
     * 获取枚举值
     *
     * @return enum value
     */
    T getValue();

}
