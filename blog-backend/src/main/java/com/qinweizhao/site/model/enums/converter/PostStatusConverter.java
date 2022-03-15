package com.qinweizhao.site.model.enums.converter;

import com.qinweizhao.site.model.enums.PostStatus;

import javax.persistence.Converter;

/**
 * PostStatus converter.
 *
 * @author johnniang
 * @date 3/27/19
 */
@Converter(autoApply = true)
public class PostStatusConverter extends AbstractConverter<PostStatus, Integer> {

}
