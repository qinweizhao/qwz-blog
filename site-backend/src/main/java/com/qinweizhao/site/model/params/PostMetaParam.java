package com.qinweizhao.site.model.params;

import com.qinweizhao.site.model.entity.PostMeta;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Post meta param.
 *
 * @author ryanwang
 * @author ikaisec
 * @date 2019-08-04
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostMetaParam extends BaseMetaParam<PostMeta> {
}
