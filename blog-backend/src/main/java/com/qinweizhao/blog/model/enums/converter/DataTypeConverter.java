package com.qinweizhao.blog.model.enums.converter;

import com.qinweizhao.blog.model.enums.DataType;

import javax.persistence.Converter;

/**
 * Data type converter.
 *
 * @author johnniang
 * @date 4/10/19
 */
@Converter(autoApply = true)
public class DataTypeConverter extends AbstractConverter<DataType, Integer> {

}
