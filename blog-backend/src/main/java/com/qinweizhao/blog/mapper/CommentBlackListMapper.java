package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.CommentBlackList;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author qinweizhao
 * @since 2022/7/6
 */
@Mapper
public interface CommentBlackListMapper extends BaseMapper<CommentBlackList> {

    /**
     * 根据IP地址获取数据
     *
     * @param ipAddress ipAddress
     * @return CommentBlackList
     */
    default CommentBlackList selectByIpAddress(String ipAddress) {
        return selectOne(new LambdaQueryWrapper<CommentBlackList>().eq(CommentBlackList::getIpAddress, ipAddress));
    }

    /**
     * 根据 IP 地址更新评论黑名单
     *
     * @param commentBlackList commentBlackList
     * @return int
     */
    default int updateByIpAddress(CommentBlackList commentBlackList) {
        return update(commentBlackList, new LambdaUpdateWrapper<CommentBlackList>().eq(CommentBlackList::getIpAddress, commentBlackList.getIpAddress()));

    }
}
