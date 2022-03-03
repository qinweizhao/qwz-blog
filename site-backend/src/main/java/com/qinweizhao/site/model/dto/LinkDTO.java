package com.qinweizhao.site.model.dto;

import lombok.Data;
import com.qinweizhao.site.model.dto.base.OutputConverter;
import com.qinweizhao.site.model.entity.Link;

/**
 * Link output dto.
 *
 * @author ryanwang
 * @date 2019/3/21
 */
@Data
public class LinkDTO implements OutputConverter<LinkDTO, Link> {

    private Integer id;

    private String name;

    private String url;

    private String logo;

    private String description;

    private String team;

    private Integer priority;
}
