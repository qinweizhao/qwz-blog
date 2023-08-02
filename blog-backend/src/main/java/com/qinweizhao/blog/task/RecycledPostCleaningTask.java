package com.qinweizhao.blog.task;

import com.qinweizhao.blog.mapper.ArticleMapper;
import com.qinweizhao.blog.model.entity.Article;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import com.qinweizhao.blog.model.enums.TimeUnit;
import com.qinweizhao.blog.service.SettingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author qinweizhao
 * @since 2020-10-19
 */
@Slf4j
@Component
@AllArgsConstructor
public class RecycledPostCleaningTask {

    private final SettingService settingService;

    private final ArticleMapper articleMapper;


    /**
     * 清理回收站文章
     */
    @Scheduled(cron = "0 0 */1 * * *")
    public synchronized void run() {
        Boolean recycledPostCleaningEnabled = Boolean.parseBoolean(String.valueOf(settingService.get("recycled_post_cleaning_enabled")));

        log.debug("{}自动清理回收状态文章", Boolean.TRUE.equals(recycledPostCleaningEnabled) ? "启用" : "禁用");

        if (Boolean.FALSE.equals(recycledPostCleaningEnabled)) {
            return;
        }

        Integer recycledPostRetentionTime = (Integer) settingService.get("recycled_post_retention_time");

        TimeUnit timeUnit = (TimeUnit) settingService.get("recycled_post_retention_timeunit");
        log.debug("{} = {}", recycledPostRetentionTime);
        log.debug("{} = {}", Objects.requireNonNull(timeUnit).name());

        long expiredIn;
        switch (timeUnit) {
            case HOUR:
                expiredIn = recycledPostRetentionTime;
                break;
            case DAY:
            default:
                expiredIn = recycledPostRetentionTime * 24L;
                break;
        }
        List<Article> recycleArticle = articleMapper.selectListByStatus(ArticleStatus.RECYCLE);

        LocalDateTime now = LocalDateTime.now();
        List<Integer> ids = recycleArticle.stream().filter(post -> {
            LocalDateTime updateTime = post.getUpdateTime();
            long until = updateTime.until(now, ChronoUnit.HOURS);
            return until >= expiredIn;
        }).map(Article::getId).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(ids)) {
            return;
        }

        log.info("开始清理回收的文章");
        int deleteNumber = articleMapper.deleteBatchIds(ids);
        log.info("清理回收状态文章已完成, {} 篇文章已被永久删除", deleteNumber);
    }

}
