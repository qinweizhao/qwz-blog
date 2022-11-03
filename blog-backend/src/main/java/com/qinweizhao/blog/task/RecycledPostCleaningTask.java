package com.qinweizhao.blog.task;

import com.qinweizhao.blog.mapper.PostMapper;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.enums.TimeUnit;
import com.qinweizhao.blog.model.properties.PostProperties;
import com.qinweizhao.blog.service.ConfigService;
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

    private final ConfigService configService;

    private final PostMapper postMapper;


    /**
     * 清理回收站文章
     */
    @Scheduled(cron = "0 0 */1 * * *")
    public synchronized void run() {
        Boolean recycledPostCleaningEnabled = configService.getByPropertyOrDefault(PostProperties.RECYCLED_POST_CLEANING_ENABLED, Boolean.class, false);

        log.debug("{}自动清理回收状态文章", Boolean.TRUE.equals(recycledPostCleaningEnabled) ? "启用" : "禁用");

        if (Boolean.FALSE.equals(recycledPostCleaningEnabled)) {
            return;
        }

        Integer recycledPostRetentionTime = configService.getByPropertyOrDefault(PostProperties.RECYCLED_POST_RETENTION_TIME, Integer.class, PostProperties.RECYCLED_POST_RETENTION_TIME.defaultValue(Integer.class));

        TimeUnit timeUnit = configService.getEnumByPropertyOrDefault(PostProperties.RECYCLED_POST_RETENTION_TIMEUNIT, TimeUnit.class, TimeUnit.DAY);
        log.debug("{} = {}", PostProperties.RECYCLED_POST_RETENTION_TIME.getValue(), recycledPostRetentionTime);
        log.debug("{} = {}", PostProperties.RECYCLED_POST_RETENTION_TIMEUNIT.getValue(), Objects.requireNonNull(timeUnit).name());

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
        List<Post> recyclePost = postMapper.selectListByStatus(PostStatus.RECYCLE);

        LocalDateTime now = LocalDateTime.now();
        List<Integer> ids = recyclePost.stream().filter(post -> {
            LocalDateTime updateTime = post.getUpdateTime();
            long until = updateTime.until(now, ChronoUnit.HOURS);
            return until >= expiredIn;
        }).map(Post::getId).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(ids)) {
            return;
        }

        log.info("开始清理回收的文章");
        int deleteNumber = postMapper.deleteBatchIds(ids);
        log.info("清理回收状态文章已完成, {} 篇文章已被永久删除", deleteNumber);
    }

}
