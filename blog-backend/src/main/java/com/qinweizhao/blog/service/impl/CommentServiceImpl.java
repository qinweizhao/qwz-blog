package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.mapper.CommentMapper;
import com.qinweizhao.blog.model.entity.Comment;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

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

    @Override
    public Page<Comment> pageComment(CommentQueryParam commentQueryParam) {
        return this.baseMapper.selectPageComment(commentQueryParam);
    }

}
