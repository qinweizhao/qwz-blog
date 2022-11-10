package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.dto.StatisticDTO;
import com.qinweizhao.blog.model.dto.StatisticWithUserDTO;

/**
 * Statistic service interface.
 *
 * @since 2019-12-16
 */
public interface StatisticService {

    /**
     * Get blog statistic.
     *
     * @return statistic dto.
     */
    StatisticDTO getStatistic();

    /**
     * Get blog statistic with user info.
     *
     * @return statistic with user info dto.
     */
    StatisticWithUserDTO getStatisticWithUser();
}
