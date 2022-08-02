package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qinweizhao.blog.convert.CommentConvert;
import com.qinweizhao.blog.convert.PostConvert;
import com.qinweizhao.blog.exception.BadRequestException;
import com.qinweizhao.blog.mapper.CommentMapper;
import com.qinweizhao.blog.mapper.PostMapper;
import com.qinweizhao.blog.model.base.PageParam;
import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.dto.post.PostSimpleDTO;
import com.qinweizhao.blog.model.entity.Comment;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.entity.User;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.model.params.PostCommentParam;
import com.qinweizhao.blog.model.projection.CommentCountProjection;
import com.qinweizhao.blog.model.properties.BlogProperties;
import com.qinweizhao.blog.model.vo.PostCommentWithPostVO;
import com.qinweizhao.blog.security.authentication.Authentication;
import com.qinweizhao.blog.security.context.SecurityContextHolder;
import com.qinweizhao.blog.service.CommentService;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.UserService;
import com.qinweizhao.blog.util.ServiceUtils;
import com.qinweizhao.blog.util.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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

    private final PostMapper postMapper;

    private final OptionService optionService;

    private final UserService userService;


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
    public List<PostCommentWithPostVO> buildResultVO(List<CommentDTO> contents) {
        // 获取 id
        Set<Integer> postIds = ServiceUtils.fetchProperty(contents, CommentDTO::getPostId);

        if (ObjectUtils.isEmpty(postIds)) {
            return new ArrayList<>();
        }

        Map<Integer, Post> postMap = ServiceUtils.convertToMap(postMapper.selectListByIds(postIds), Post::getId);

        return contents.stream()
                .filter(comment -> postMap.containsKey(comment.getPostId()))
                .map(comment -> {

                    PostCommentWithPostVO postCommentWithPostVO = CommentConvert.INSTANCE.convertPostToVO(comment);

                    Post post = postMap.get(comment.getPostId());
                    PostSimpleDTO postSimpleDTO = PostConvert.INSTANCE.convert(post);

                    postCommentWithPostVO.setPost(postSimpleDTO);

                    return postCommentWithPostVO;
                }).collect(Collectors.toList());

    }

    @Override
    public PageResult<PostCommentWithPostVO> buildPageResultVO(PageResult<CommentDTO> commentResult) {
        List<CommentDTO> contents = commentResult.getContent();
        return new PageResult<>(this.buildResultVO(contents), commentResult.getTotal(), commentResult.hasPrevious(), commentResult.hasNext());
    }

    @Override
    public boolean updateStatus(Long commentId, CommentStatus status) {
        return commentMapper.updateStatusById(commentId, status);
    }

    @Override
    public boolean save(PostCommentParam commentParam) {

        // 检查用户登录状态并设置此字段
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            // Blogger comment
            User user = authentication.getDetail().getUser();
            commentParam.setAuthor(StringUtils.isBlank(user.getNickname()) ? user.getUsername() : user.getNickname());
            commentParam.setEmail(user.getEmail());
            commentParam.setAuthorUrl(optionService.getByPropertyOrDefault(BlogProperties.BLOG_URL, String.class, null));
        }

        // 手动验证评论参数
        ValidationUtils.validate(commentParam);

        if (authentication == null) {

            if (userService.getByEmail(commentParam.getEmail()).isPresent()) {
                throw new BadRequestException("不能使用博主的邮箱，如果您是博主，请登录管理端进行回复。");
            }
        }

        int i = commentMapper.insert(CommentConvert.INSTANCE.convert(commentParam));
        return i > 0;
    }

    @Override
    public boolean removeById(Long commentId) {

        Comment comment = commentMapper.selectById(commentId);

        List<Comment> children = this.listChildren(comment.getPostId(), commentId);

        if (children.size() > 0) {
            children.forEach(child -> commentMapper.deleteById(child.getId()));
        }

        return commentMapper.deleteById(commentId) > 0;
    }

    /**
     * 查找子评论
     *
     * @param targetId        postId
     * @param commentParentId commentParentId
     * @return List
     */
    private List<Comment> listChildren(Integer targetId, Long commentParentId) {

        List<Comment> directChildren = commentMapper.selectListByPostIdAndParentId(targetId, commentParentId);

        // 创建结果容器
        Set<Comment> children = new HashSet<>();

        this.getChildrenRecursively(directChildren, children);

        List<Comment> childrenList = new ArrayList<>(children);
        childrenList.sort(Comparator.comparing(Comment::getId));

        return childrenList;

    }

    private void getChildrenRecursively(List<Comment> topComments, Set<Comment> children) {

        if (CollectionUtils.isEmpty(topComments)) {
            return;
        }

        Set<Long> commentIds = ServiceUtils.fetchProperty(topComments, Comment::getId);

        // Get direct children
        List<Comment> directChildren = commentMapper.selectListByParentIds(commentIds);

        // Recursively invoke
        getChildrenRecursively(directChildren, children);

        // Add direct children to children result
        children.addAll(topComments);
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

    @Override
    public Long count() {
        return commentMapper.selectCount(Wrappers.emptyWrapper());
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
        int total = comments.size();
        int pageNum = param.getPage();
        int pageSize = param.getSize();
        boolean hasPrevious = param.getPage() > 1;
        boolean hasNext = (total - (pageNum * pageSize)) > pageSize;

        return new PageResult<>(pageContent, topComments.size(), hasPrevious, hasNext);
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
