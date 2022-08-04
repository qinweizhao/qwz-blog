package com.qinweizhao.blog.model.vo;

import com.qinweizhao.blog.model.dto.MetaDTO;
import com.qinweizhao.blog.model.dto.post.PostDetailDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Set;

/**
 * Sheet detail VO.
 *
 * @author ryanwang
 * @date 2019-12-10
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SheetDetailVO extends PostDetailDTO {

    private Set<Long> metaIds;

    private List<MetaDTO> metas;
}
