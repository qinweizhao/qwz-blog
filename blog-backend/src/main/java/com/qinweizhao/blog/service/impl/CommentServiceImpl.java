package com.qinweizhao.blog.service.impl;

import cn.hutool.core.util.URLUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qinweizhao.blog.exception.BadRequestException;
import com.qinweizhao.blog.exception.ForbiddenException;
import com.qinweizhao.blog.exception.NotFoundException;
import com.qinweizhao.blog.mapper.CommentMapper;
import com.qinweizhao.blog.mapper.JournalMapper;
import com.qinweizhao.blog.mapper.PostMapper;
import com.qinweizhao.blog.model.convert.CommentConvert;
import com.qinweizhao.blog.model.core.PageParam;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.entity.Comment;
import com.qinweizhao.blog.model.entity.Journal;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.entity.User;
import com.qinweizhao.blog.model.enums.*;
import com.qinweizhao.blog.model.param.CommentParam;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.model.projection.CommentCountProjection;
import com.qinweizhao.blog.model.properties.BlogProperties;
import com.qinweizhao.blog.model.properties.CommentProperties;
import com.qinweizhao.blog.framework.security.authentication.Authentication;
import com.qinweizhao.blog.framework.security.context.SecurityContextHolder;
import com.qinweizhao.blog.service.CommentBlackListService;
import com.qinweizhao.blog.service.CommentService;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.UserService;
import com.qinweizhao.blog.util.ServiceUtils;
import com.qinweizhao.blog.util.ServletUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.qinweizhao.blog.model.support.HaloConst.URL_SEPARATOR;

/**
 * PostCommentService implementation class
 *
 * @author ryanwang
 * @author johnniang
 * @author qinweizhao
 * @since 2019-03-14
 */
@Slf4j
@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {


    private final CommentMapper commentMapper;

    private final PostMapper postMapper;

    private final JournalMapper journalMapper;

    private final ConfigService configService;

    private final UserService userService;

    private final CommentBlackListService commentBlackListService;

    @Override
    public void validateCommentBlackListStatus() {
        CommentViolationTypeEnum banStatus = commentBlackListService.commentsBanStatus(ServletUtils.getRequestIp());
        Integer banTime = configService.getByPropertyOrDefault(CommentProperties.COMMENT_BAN_TIME, Integer.class, 10);
        if (banStatus == CommentViolationTypeEnum.FREQUENTLY) {
            throw new ForbiddenException(String.format("您的评论过于频繁，请%s分钟之后再试。", banTime));
        }
    }

    @Override
    public long countByStatus(CommentStatus published) {
        return commentMapper.selectCountByStatus(published);
    }

    @Override
    public PageResult<CommentDTO> pageComment(CommentQueryParam param) {
        PageResult<Comment> commentResult = commentMapper.selectPageComments(param);
        List<Comment> contents = commentResult.getContent();

        List<CommentDTO> result = CommentConvert.INSTANCE.convertToDTO(contents);

        Set<Integer> targetIds = ServiceUtils.fetchProperty(contents, Comment::getTargetId);

        List<CommentDTO> collect;

        // 分页时有类型
        CommentType type = param.getType();
        if (type.equals(CommentType.POST)) {
            Map<Integer, Post> postMap = ServiceUtils.convertToMap(postMapper.selectListByIds(targetIds), Post::getId);
            collect = result.stream()
                    .filter(comment -> postMap.containsKey(comment.getTargetId()))
                    .peek(
                            comment -> {
                                Integer targetId = comment.getTargetId();
                                Post post = postMap.get(targetId);
                                Map<String, Object> map = new HashMap<>(3);
                                map.put("title", post.getTitle());
                                map.put("status", ValueEnum.valueToEnum(PostStatus.class, post.getStatus()));
                                map.put("fullPath", this.buildFullPath(post.getId()));
                                comment.setTarget(map);
                            }
                    ).collect(Collectors.toList());

        } else {
            Map<Integer, Journal> journalMap = ServiceUtils.convertToMap(journalMapper.selectListByIds(targetIds), Journal::getId);
            collect = result.stream()
                    .filter(comment -> journalMap.containsKey(comment.getTargetId())).peek(
                            comment -> {
                                Integer targetId = comment.getTargetId();
                                Journal journal = journalMap.get(targetId);
                                Map<String, Object> map = new HashMap<>(2);
                                map.put("content", journal.getContent());
                                map.put("createTime", journal.getCreateTime());
                                comment.setTarget(map);
                            }
                    ).collect(Collectors.toList());
        }

        return new PageResult<>(collect, commentResult.getTotal(), commentResult.hasPrevious(), commentResult.hasNext());
    }

    public String buildFullPath(Integer postId) {
        StringBuilder fullPath = new StringBuilder();

        if (configService.isEnabledAbsolutePath()) {
            fullPath.append(configService.getBlogBaseUrl());
        }

        fullPath.append(URL_SEPARATOR);

        fullPath.append("?p=")
                .append(postId);

        return fullPath.toString();
    }


    @Override
    public boolean updateStatus(Long commentId, CommentStatus status) {
        return commentMapper.updateStatusById(commentId, status);
    }

    @Override
    public boolean save(CommentParam param) {
        CommentType type = param.getType();

        // 检查用户登录状态并设置此字段
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            User user = authentication.getDetail().getUser();
            param.setAuthor(StringUtils.isBlank(user.getNickname()) ? user.getUsername() : user.getNickname());
            param.setEmail(user.getEmail());
            param.setAuthorUrl(configService.getByPropertyOrDefault(BlogProperties.BLOG_URL, String.class, null));
        }


        if (authentication == null) {
            if (userService.getByEmail(param.getEmail()).isPresent()) {
                throw new BadRequestException("不能使用博主的邮箱，如果您是博主，请登录管理端进行回复。");
            }
        }

        Comment comment = CommentConvert.INSTANCE.convert(param);

        // Check post id
        if (!ServiceUtils.isEmptyId(comment.getTargetId())) {
            this.validateTarget(comment.getTargetId(), type);
        }

        // Check parent id
        if (!ServiceUtils.isEmptyId(comment.getParentId())) {
            Comment flag = commentMapper.selectById(comment.getParentId());
            // 不存在抛出异常
            if (ObjectUtils.isEmpty(flag)) {
                throw new NotFoundException("父评论不存在");
            }
        }

        // Set some default values
        if (comment.getIpAddress() == null) {
            comment.setIpAddress(ServletUtils.getRequestIp());
        }

        if (comment.getUserAgent() == null) {
            comment.setUserAgent(ServletUtils.getHeaderIgnoreCase(HttpHeaders.USER_AGENT));
        }

        if (comment.getGravatarMd5() == null) {
            comment.setGravatarMd5(DigestUtils.md5Hex(comment.getEmail()));
        }

        if (StringUtils.isNotEmpty(comment.getAuthorUrl())) {
            comment.setAuthorUrl(URLUtil.normalize(comment.getAuthorUrl()));
        }

        if (authentication != null) {
            // 博主的评论
            comment.setIsAdmin(true);
            comment.setStatus(CommentStatus.PUBLISHED.getValue());
        } else {
            // 游客的评论
            // 处理评论状态
            Boolean needAudit = configService.getByPropertyOrDefault(CommentProperties.NEW_NEED_CHECK, Boolean.class, true);
            comment.setStatus(needAudit ? CommentStatus.AUDITING.getValue() : CommentStatus.PUBLISHED.getValue());
        }

        // 设置类型
        comment.setType(type.getValue());

        // todo
        // event

        int i = commentMapper.insert(comment);
        return i > 0;
    }

    @Override
    public void validateTarget(Integer targetId, CommentType type) {

        if (type.equals(CommentType.POST)) {
            Post post = postMapper.selectById(targetId);
            Assert.notNull(post, "查询不到该文章的信息");
            if (post.getDisallowComment()) {
                throw new BadRequestException("该文章已经被禁止评论").setErrorData(targetId);
            }
        } else {
            if (!journalMapper.selectExistsById(targetId)) {
                throw new NotFoundException("查询不到该日志信息").setErrorData(targetId);
            }
        }

    }

    @Override
    public boolean removeByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }

        ids.forEach(this::removeById);
        return true;
    }

    @Override
    public List<CommentDTO> listLatest(CommentQueryParam param) {
        return this.pageComment(param).getContent();
    }

    @Override
    public CommentDTO getById(Long commentId) {
        Comment comment = commentMapper.selectById(commentId);
        return CommentConvert.INSTANCE.convert(comment);
    }

    @Override
    public boolean removeById(Long commentId) {

        Comment comment = commentMapper.selectById(commentId);

        List<Comment> children = this.listChildren(comment.getTargetId(), commentId);

        if (children.size() > 0) {
            children.forEach(child -> commentMapper.deleteById(child.getId()));
        }

        return commentMapper.deleteById(commentId) > 0;
    }

    @Override
    public boolean updateStatusByIds(List<Long> ids, CommentStatus status) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }

        ids.forEach(id -> commentMapper.updateStatusById(id, status));

        return true;
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
    public Map<Integer, Long> countByTypeAndTargetIds(CommentType type, Set<Integer> postIds) {
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyMap();
        }
        List<CommentCountProjection> commentCountProjections = commentMapper.selectCountByTypeAndTargetIds(type,postIds);
        return ServiceUtils.convertToMap(commentCountProjections, CommentCountProjection::getTargetId, CommentCountProjection::getCount);
    }

    @Override
    public PageResult<CommentDTO> pageTree(Integer targetId, CommentQueryParam param) {

        List<Comment> comments = commentMapper.selectListByTypeAndTargetId(param.getType(),targetId);

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
        Assert.notNull(parentComment, "父评论不能为空");

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
            CommentDTO commentDTO = CommentConvert.INSTANCE.convert(comment);

            if (parentComment.getChildren() == null) {
                parentComment.setChildren(new LinkedList<>());
            }

            parentComment.getChildren().add(commentDTO);
        });

        // Remove children
        comments.removeAll(children);

        if (!CollectionUtils.isEmpty(parentComment.getChildren())) {
            // Recursively concrete the children
            parentComment.getChildren().forEach(childComment -> concreteTree(childComment, comments));

        }
    }


}
