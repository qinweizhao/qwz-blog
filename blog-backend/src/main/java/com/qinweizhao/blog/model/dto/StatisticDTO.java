package com.qinweizhao.blog.model.dto;

import lombok.Data;

/**
 * Statistic DTO.
 *
 * @since 2019-03-19
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
