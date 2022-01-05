package com.qinweizhao.site.model.params;

import com.qinweizhao.site.model.entity.SheetMeta;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Sheet meta param.
 *
 * @author ryanwang
 * @author ikaisec
 * @date 2019-08-04
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SheetMetaParam extends BaseMetaParam<SheetMeta> {
}
