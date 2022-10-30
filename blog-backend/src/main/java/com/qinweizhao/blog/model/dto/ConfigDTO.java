package com.qinweizhao.blog.model.dto;


import lombok.Data;

/**
 * Option output dto.
 *
 * @author johnniang
 * @since 3/20/19
 */
@Data
public class ConfigDTO {

    private Integer id;

    private String key;

    private Object value;

}
