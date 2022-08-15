package com.qinweizhao.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Option output dto.
 *
 * @author johnniang
 * @since 3/20/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDTO {

    private String key;

    private Object value;

}
