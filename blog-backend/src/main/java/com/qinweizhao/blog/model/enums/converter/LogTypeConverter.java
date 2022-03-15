package com.qinweizhao.blog.model.enums.converter;

import com.qinweizhao.blog.model.enums.LogType;

import javax.persistence.Converter;

/**
 * Log type converter.
 *
 * @author johnniang
 * @date 3/27/19
 */
@Converter(autoApply = true)
public class LogTypeConverter extends AbstractConverter<LogType, Integer> {

}
