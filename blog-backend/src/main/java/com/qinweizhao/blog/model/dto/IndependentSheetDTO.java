package com.qinweizhao.blog.model.dto;

import lombok.Data;

/**
 * Theme controller.
 *
 * @author ryanwang
 * @since 2019/5/4
 */
@Data
public class IndependentSheetDTO {

    private Integer id;

    private String title;

    private String fullPath;

    private String routeName;

    private Boolean available;
}
