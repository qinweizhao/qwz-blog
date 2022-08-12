package com.qinweizhao.blog.service.impl;

import com.qinweizhao.blog.mapper.CommentBlackListMapper;
import com.qinweizhao.blog.mapper.CommentMapper;
import com.qinweizhao.blog.model.entity.CommentBlackList;
import com.qinweizhao.blog.model.enums.CommentViolationTypeEnum;
import com.qinweizhao.blog.model.properties.CommentProperties;
import com.qinweizhao.blog.service.CommentBlackListService;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.util.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

/**
 * Comment BlackList Service Implements
 *
 * @author Lei XinXin
 * @author qinweizhao
 * @since 2020/1/3
 */
@Slf4j
@Service
@AllArgsConstructor
public class CommentBlackListServiceImpl implements CommentBlackListService {

    private final CommentBlackListMapper commentBlackListMapper;

    private final CommentMapper commentMapper;

    private final OptionService optionService;


    @Override
    public CommentViolationTypeEnum commentsBanStatus(String ipAddress) {
        /*
        N=后期可配置
        1. 获取评论次数；
        2. 判断N分钟内，是否超过规定的次数限制，超过后需要每隔N分钟才能再次评论；
        3. 如果在时隔N分钟内，还有多次评论，可被认定为恶意攻击者；
        4. 对恶意攻击者进行N分钟的封禁；
        */
        CommentBlackList blackList = commentBlackListMapper.selectByIpAddress(ipAddress);
        LocalDateTime now = LocalDateTime.now();
        Date endTime = new Date(DateTimeUtils.toEpochMilli(now));
        Integer banTime = optionService.getByPropertyOrDefault(CommentProperties.COMMENT_BAN_TIME, Integer.class, 10);
        Date startTime = new Date(DateTimeUtils.toEpochMilli(now.minusMinutes(banTime)));
        Integer range = optionService.getByPropertyOrDefault(CommentProperties.COMMENT_RANGE, Integer.class, 30);
        boolean isPresent = commentMapper.countByIpAndTime(ipAddress, startTime, endTime) >= range;
        if (isPresent && !ObjectUtils.isEmpty(blackList)) {
            update(now, blackList, banTime);
            return CommentViolationTypeEnum.FREQUENTLY;
        } else if (isPresent) {
            CommentBlackList commentBlackList = new CommentBlackList();
            commentBlackList.setBanTime(getBanTime(now, banTime));
            commentBlackList.setIpAddress(ipAddress);

            commentBlackListMapper.insert(commentBlackList);
            return CommentViolationTypeEnum.FREQUENTLY;
        }
        return CommentViolationTypeEnum.NORMAL;
    }

    private void update(LocalDateTime localDateTime, CommentBlackList blackList, Integer banTime) {
        blackList.setBanTime(getBanTime(localDateTime, banTime));
        int updateResult = commentBlackListMapper.updateByIpAddress(blackList);
        Optional.of(updateResult)
                .filter(result -> result <= 0).ifPresent(result -> log.error("更新评论封禁时间失败"));
    }

    private LocalDateTime getBanTime(LocalDateTime localDateTime, Integer banTime) {
        return localDateTime.plusMinutes(banTime);
    }
}
