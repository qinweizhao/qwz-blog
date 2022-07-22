package com.qinweizhao.blog.service.impl;

import com.qinweizhao.blog.convert.CommentConvert;
import com.qinweizhao.blog.mapper.CommentMapper;
import com.qinweizhao.blog.model.base.PageParam;
import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.entity.Comment;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.model.projection.CommentCountProjection;
import com.qinweizhao.blog.service.CommentService;
import com.qinweizhao.blog.util.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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
public class CommentServiceImpl implements CommentService {


    private final CommentMapper commentMapper;
//
//    private final PostService postService;

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
        return commentMapper.selectCountByStatus(published);
    }

    @Override
    public PageResult<CommentDTO> pageComment(CommentQueryParam commentQueryParam) {
        PageResult<Comment> commentResult = commentMapper.selectPageComments(commentQueryParam);
        return CommentConvert.INSTANCE.convertToDTO(commentResult);

    }

    @Override
    public Map<Integer, Long> countByPostIds(Set<Integer> postIds) {
        List<CommentCountProjection> commentCountProjections = commentMapper.selectCountByPostIds(postIds);
        return ServiceUtils.convertToMap(commentCountProjections, CommentCountProjection::getPostId, CommentCountProjection::getCount);
    }

    @Override
    public PageResult<CommentDTO> pageTree(Integer postId, PageParam param) {

        List<Comment> comments = commentMapper.selectListByPostId(postId);

        return this.buildPageTree(comments, param);
    }

    /**
     * 构建分页树
     *
     * @param comments comments
     * @param param    param
     * @return PageResult
     */
    private PageResult<CommentDTO> buildPageTree(List<Comment> comments, PageParam param) {

        // 初始化置顶虚拟评论
        CommentDTO topVirtualComment = new CommentDTO();
        topVirtualComment.setId(0L);
        topVirtualComment.setChildren(new LinkedList<>());

        // 构建树
        this.concreteTree(topVirtualComment, comments);
        List<CommentDTO> topComments = topVirtualComment.getChildren();
        List<CommentDTO> pageContent;

        // 构建分页
        int startIndex = (param.getPage() - 1) * param.getSize();
        if (startIndex >= topComments.size() || startIndex < 0) {
            pageContent = Collections.emptyList();
        } else {
            int endIndex = startIndex + param.getSize();
            if (endIndex > topComments.size()) {
                endIndex = topComments.size();
            }

            log.debug("Top comments size: [{}]", topComments.size());
            log.debug("Start index: [{}]", startIndex);
            log.debug("End index: [{}]", endIndex);

            pageContent = topComments.subList(startIndex, endIndex);
        }
        return new PageResult<>(pageContent, topComments.size());
    }


    /**
     * 构建具体的树
     *
     * @param parentComment parentComment
     * @param comments      comments
     */
    private void concreteTree(CommentDTO parentComment, List<Comment> comments) {
        Assert.notNull(parentComment, "Parent comment must not be null");

        if (CollectionUtils.isEmpty(comments)) {
            return;
        }

        // Get children
        List<Comment> children = comments.stream()
                .filter(comment -> Objects.equals(parentComment.getId(), comment.getParentId()))
                .collect(Collectors.toList());

        // Add children
        children.forEach(comment -> {
            // Convert to comment vo
            CommentDTO commentVO = CommentConvert.INSTANCE.convert(comment);

            if (parentComment.getChildren() == null) {
                parentComment.setChildren(new LinkedList<>());
            }

            parentComment.getChildren().add(commentVO);
        });

        // Remove children
        comments.removeAll(children);

        if (!CollectionUtils.isEmpty(parentComment.getChildren())) {
            // Recursively concrete the children
            parentComment.getChildren().forEach(childComment -> concreteTree(childComment, comments));

        }
    }


}
