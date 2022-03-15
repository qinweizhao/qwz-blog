package com.qinweizhao.site.model.enums.converter;

import com.qinweizhao.site.model.enums.DataType;

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
