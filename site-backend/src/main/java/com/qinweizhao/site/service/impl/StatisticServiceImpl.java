package com.qinweizhao.site.service.impl;

import org.springframework.stereotype.Service;
import com.qinweizhao.site.exception.ServiceException;
import com.qinweizhao.site.model.dto.StatisticDTO;
import com.qinweizhao.site.model.dto.StatisticWithUserDTO;
import com.qinweizhao.site.model.dto.UserDTO;
import com.qinweizhao.site.model.entity.User;
import com.qinweizhao.site.model.enums.CommentStatus;
import com.qinweizhao.site.model.enums.PostStatus;
import com.qinweizhao.site.service.CategoryService;
import com.qinweizhao.site.service.JournalCommentService;
import com.qinweizhao.site.service.JournalService;
import com.qinweizhao.site.service.LinkService;
import com.qinweizhao.site.service.OptionService;
import com.qinweizhao.site.service.PostCommentService;
import com.qinweizhao.site.service.PostService;
import com.qinweizhao.site.service.SheetCommentService;
import com.qinweizhao.site.service.SheetService;
import com.qinweizhao.site.service.StatisticService;
import com.qinweizhao.site.service.TagService;
import com.qinweizhao.site.service.UserService;

/**
 * Statistic service implementation.
 *
 * @author ryanwang
 * @date 2019-12-16
 */
@Service
public class StatisticServiceImpl implements StatisticService {

    private final PostService postService;

    private final SheetService sheetService;

    private final JournalService journalService;

    private final PostCommentService postCommentService;

    private final SheetCommentService sheetCommentService;

    private final JournalCommentService journalCommentService;

    private final OptionService optionService;

    private final LinkService linkService;

    private final CategoryService categoryService;

    private final TagService tagService;

    private final UserService userService;

    public StatisticServiceImpl(PostService postService,
        SheetService sheetService,
        JournalService journalService,
        PostCommentService postCommentService,
        SheetCommentService sheetCommentService,
        JournalCommentService journalCommentService,
        OptionService optionService,
        LinkService linkService,
        CategoryService categoryService,
        TagService tagService,
        UserService userService) {
        this.postService = postService;
        this.sheetService = sheetService;
        this.journalService = journalService;
        this.postCommentService = postCommentService;
        this.sheetCommentService = sheetCommentService;
        this.journalCommentService = journalCommentService;
        this.optionService = optionService;
        this.linkService = linkService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @Override
    public StatisticDTO getStatistic() {
        StatisticDTO statisticDto = new StatisticDTO();
        statisticDto.setPostCount(postService.countByStatus(PostStatus.PUBLISHED));

        // Handle comment count
        long postCommentCount = postCommentService.countByStatus(CommentStatus.PUBLISHED);
        long sheetCommentCount = sheetCommentService.countByStatus(CommentStatus.PUBLISHED);
        long journalCommentCount = journalCommentService.countByStatus(CommentStatus.PUBLISHED);

        statisticDto.setCommentCount(postCommentCount + sheetCommentCount + journalCommentCount);
        statisticDto.setTagCount(tagService.count());
        statisticDto.setCategoryCount(categoryService.count());
        statisticDto.setJournalCount(journalService.count());

        long birthday = optionService.getBirthday();
        long days = (System.currentTimeMillis() - birthday) / (1000 * 24 * 3600);
        statisticDto.setEstablishDays(days);
        statisticDto.setBirthday(birthday);

        statisticDto.setLinkCount(linkService.count());
        statisticDto.setVisitCount(postService.countVisit() + sheetService.countVisit());
        statisticDto.setLikeCount(postService.countLike() + sheetService.countLike());
        return statisticDto;
    }

    @Override
    public StatisticWithUserDTO getStatisticWithUser() {

        StatisticDTO statisticDto = getStatistic();

        StatisticWithUserDTO statisticWithUserDto = new StatisticWithUserDTO();
        statisticWithUserDto.convertFrom(statisticDto);

        User user =
            userService.getCurrentUser().orElseThrow(() -> new ServiceException("未查询到博主信息"));
        statisticWithUserDto.setUser(new UserDTO().convertFrom(user));

        return statisticWithUserDto;
    }
}
