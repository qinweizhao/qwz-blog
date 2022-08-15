package com.qinweizhao.blog.service.impl;

import com.qinweizhao.blog.model.convert.StatisticConvert;
import com.qinweizhao.blog.model.convert.UserConvert;
import com.qinweizhao.blog.model.dto.StatisticDTO;
import com.qinweizhao.blog.model.dto.StatisticWithUserDTO;
import com.qinweizhao.blog.model.entity.User;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.security.util.SecurityUtils;
import com.qinweizhao.blog.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 统计
 *
 * @author qinweizhao
 * @since 2022/7/12
 */
@Service
@AllArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final PostService postService;

    private final JournalService journalService;

    private final CommentService commentService;

    private final ConfigService configService;

    private final CategoryService categoryService;

    private final TagService tagService;


    @Override
    public StatisticDTO getStatistic() {
        StatisticDTO statisticDTO = new StatisticDTO();
        // 文章个数
        statisticDTO.setPostCount(postService.countByStatus(PostStatus.PUBLISHED));

        // 评论个数
        long commentCount = commentService.countByStatus(CommentStatus.PUBLISHED);

        statisticDTO.setCommentCount(commentCount);
        statisticDTO.setTagCount(tagService.count());
        statisticDTO.setCategoryCount(categoryService.count());
        statisticDTO.setJournalCount(journalService.count());

        long birthday = configService.getBirthday();
        long days = (System.currentTimeMillis() - birthday) / (1000 * 24 * 3600);
        statisticDTO.setEstablishDays(days);
        statisticDTO.setBirthday(birthday);
        statisticDTO.setVisitCount(postService.countVisit());
        statisticDTO.setLikeCount(postService.countLike());
        return statisticDTO;
    }

    @Override
    public StatisticWithUserDTO getStatisticWithUser() {

        StatisticDTO statisticDTO = this.getStatistic();
        StatisticWithUserDTO statisticWithUserDTO = StatisticConvert.INSTANCE.convert(statisticDTO);

        User user = SecurityUtils.getLoginUser();
        statisticWithUserDTO.setUser(UserConvert.INSTANCE.convert(user));

        return statisticWithUserDTO;
    }

}
