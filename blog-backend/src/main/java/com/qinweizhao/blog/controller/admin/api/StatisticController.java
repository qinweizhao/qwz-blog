package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.model.dto.StatisticDTO;
import com.qinweizhao.blog.model.dto.StatisticWithUserDTO;
import com.qinweizhao.blog.service.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计
 *
 * @author ryanwang
 * @author qinweizhao
 * @since 2019-12-16
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/statistics")
public class StatisticController {

    private final StatisticService statisticService;


    /**
     * 获取统计信息(登陆后请求)
     *
     * @return StatisticDTO
     */
    @GetMapping
    public StatisticDTO statistics() {
        return statisticService.getStatistic();
    }

    /**
     * 获取用户的博客统计信息
     *
     * @return StatisticWithUserDTO
     */
    @GetMapping("user")
    public StatisticWithUserDTO statisticsWithUser() {
        return statisticService.getStatisticWithUser();
    }
}
