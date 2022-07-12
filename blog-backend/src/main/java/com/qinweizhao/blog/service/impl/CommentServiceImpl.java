package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.exception.BadRequestException;
import com.qinweizhao.blog.exception.ForbiddenException;
import com.qinweizhao.blog.mapper.CommentMapper;
import com.qinweizhao.blog.mapper.PostMapper;
import com.qinweizhao.blog.model.base.BaseEntity;
import com.qinweizhao.blog.model.entity.Comment;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.enums.CommentViolationTypeEnum;
import com.qinweizhao.blog.model.params.CommentQuery;
import com.qinweizhao.blog.model.properties.CommentProperties;
import com.qinweizhao.blog.service.CommentBlackListService;
import com.qinweizhao.blog.service.CommentService;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.utils.ServletUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * PostCommentService implementation class
 *
 * @author ryanwang
 * @author johnniang
 * @author qinweizhao
 * @date 2019-03-14
 */
@Slf4j
@Service
@AllArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {


//    private final PostMapper postMapper;
//
//    private final OptionService optionService;
//
//    private final CommentBlackListService commentBlackListService;


//    @Override
//    public void validateTarget(Integer postId) {
//        Post post = postMapper.selectById(postId);
//        Assert.notNull(post, "查询不到该文章的信息");
//        if (post.getDisallowComment()) {
//            throw new BadRequestException("该文章已经被禁止评论").setErrorData(postId);
//        }
//    }
//
//    @Override
//    public Page<Comment> page(Pageable pageable, CommentQuery commentQuery) {
//        return null;
//    }
//
//    @Override
//    public void validateCommentBlackListStatus() {
//        CommentViolationTypeEnum banStatus = commentBlackListService.commentsBanStatus(ServletUtils.getRequestIp());
//        Integer banTime = optionService.getByPropertyOrDefault(CommentProperties.COMMENT_BAN_TIME, Integer.class, 10);
//        if (banStatus == CommentViolationTypeEnum.FREQUENTLY) {
//            throw new ForbiddenException(String.format("您的评论过于频繁，请%s分钟之后再试。", banTime));
//        }
//    }

    @Override
    public long countByStatus(CommentStatus published) {
        return this.baseMapper.selectCountByStatus(published);
    }

}
