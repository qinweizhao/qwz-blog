package com.qinweizhao.blog.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-03-21
 */
@Data
public class PhotoDTO {

    private Integer id;

    private String name;

    private String thumbnail;

    private Date takeTime;

    private String url;

    private String team;

    private String location;

    private String description;
}
