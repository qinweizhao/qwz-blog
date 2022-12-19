package com.qinweizhao.blog.model.dto;

import lombok.Data;

/**
 * Statistic DTO.
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
public class StatisticDTO {

    private Long postCount;

    private Long commentCount;

    private Long categoryCount;

    private Long tagCount;

    private Long journalCount;

    private Long visitCount;

    private Long likeCount;
}
