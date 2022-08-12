package com.qinweizhao.blog.model.dto;

import lombok.Data;

/**
 * @author ryanwang
 * @since 2019-05-25
 */
@Data
public class BackupDTO {

    private String downloadLink;

    private String filename;

    private Long updateTime;

    private Long fileSize;
}
