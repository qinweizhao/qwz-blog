package com.qinweizhao.site.service.impl;

import static com.qinweizhao.site.model.support.HaloConst.URL_SEPARATOR;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.qinweizhao.site.exception.BadRequestException;
import com.qinweizhao.site.exception.ForbiddenException;
import com.qinweizhao.site.exception.NotFoundException;
import com.qinweizhao.site.model.dto.post.BasePostMinimalDTO;
import com.qinweizhao.site.model.entity.Post;
import com.qinweizhao.site.model.entity.PostComment;
import com.qinweizhao.site.model.enums.CommentViolationTypeEnum;
import com.qinweizhao.site.model.enums.PostPermalinkType;
import com.qinweizhao.site.model.properties.CommentProperties;
import com.qinweizhao.site.model.vo.PostCommentWithPostVO;
import com.qinweizhao.site.repository.PostCommentRepository;
import com.qinweizhao.site.repository.PostRepository;
import com.qinweizhao.site.service.CommentBlackListService;
import com.qinweizhao.site.service.OptionService;
import com.qinweizhao.site.service.PostCommentService;
import com.qinweizhao.site.service.UserService;
import com.qinweizhao.site.utils.DateUtils;
import com.qinweizhao.site.utils.ServiceUtils;
import com.qinweizhao.site.utils.ServletUtils;

/**
 * PostCommentService implementation class
 *
 * @author ryanwang
 * @author johnniang
 * @date 2019-03-14
 */
@Slf4j
@Service
public class PostCommentServiceImpl extends BaseCommentServiceImpl<PostComment>
    implements PostCommentService {

    private final PostRepository postRepository;

    private final CommentBlackListService commentBlackListService;

    public PostCommentServiceImpl(PostCommentRepository postCommentRepository,
        PostRepository postRepository,
        UserService userService,
        OptionService optionService,
        CommentBlackListService commentBlackListService,
        ApplicationEventPublisher eventPublisher) {
        super(postCommentRepository, optionService, userService, eventPublisher);
        this.postRepository = postRepository;
        this.commentBlackListService = commentBlackListService;
    }

    @Override
    @NonNull
    public Page<PostCommentWithPostVO> convertToWithPostVo(@NonNull Page<PostComment> commentPage) {
        Assert.notNull(commentPage, "PostComment page must not be null");

        return new PageImpl<>(convertToWithPostVo(commentPage.getContent()),
            commentPage.getPageable(), commentPage.getTotalElements());

    }

    @Override
    @NonNull
    public PostCommentWithPostVO convertToWithPostVo(@NonNull PostComment comment) {
        Assert.notNull(comment, "PostComment must not be null");
        PostCommentWithPostVO postCommentWithPostVo =
            new PostCommentWithPostVO().convertFrom(comment);

        BasePostMinimalDTO basePostMinimalDto =
            new BasePostMinimalDTO().convertFrom(postRepository.getById(comment.getPostId()));

        postCommentWithPostVo.setPost(buildPostFullPath(basePostMinimalDto));

        postCommentWithPostVo.setAvatar(buildAvatarUrl(comment.getGravatarMd5()));

        return postCommentWithPostVo;
    }

    @Override
    @NonNull
    public List<PostCommentWithPostVO> convertToWithPostVo(List<PostComment> postComments) {
        if (CollectionUtils.isEmpty(postComments)) {
            return Collections.emptyList();
        }

        // Fetch goods ids
        Set<Integer> postIds = ServiceUtils.fetchProperty(postComments, PostComment::getPostId);

        // Get all posts
        Map<Integer, Post> postMap =
            ServiceUtils.convertToMap(postRepository.findAllById(postIds), Post::getId);

        return postComments.stream()
            .filter(comment -> postMap.containsKey(comment.getPostId()))
            .map(comment -> {
                // Convert to vo
                PostCommentWithPostVO postCommentWithPostVo =
                    new PostCommentWithPostVO().convertFrom(comment);

                BasePostMinimalDTO basePostMinimalDto =
                    new BasePostMinimalDTO().convertFrom(postMap.get(comment.getPostId()));

                postCommentWithPostVo.setPost(buildPostFullPath(basePostMinimalDto));

                postCommentWithPostVo.setAvatar(buildAvatarUrl(comment.getGravatarMd5()));

                return postCommentWithPostVo;
            }).collect(Collectors.toList());
    }

    private BasePostMinimalDTO buildPostFullPath(BasePostMinimalDTO post) {
        PostPermalinkType permalinkType = optionService.getPostPermalinkType();

        String pathSuffix = optionService.getPathSuffix();

        String archivesPrefix = optionService.getArchivesPrefix();

        int month = DateUtils.month(post.getCreateTime()) + 1;

        String monthString = month < 10 ? "0" + month : String.valueOf(month);

        int day = DateUtils.dayOfMonth(post.getCreateTime());

        String dayString = day < 10 ? "0" + day : String.valueOf(day);

        StringBuilder fullPath = new StringBuilder();

        if (optionService.isEnabledAbsolutePath()) {
            fullPath.append(optionService.getBlogBaseUrl());
        }

        fullPath.append(URL_SEPARATOR);

        if (permalinkType.equals(PostPermalinkType.DEFAULT)) {
            fullPath.append(archivesPrefix)
                .append(URL_SEPARATOR)
                .append(post.getSlug())
                .append(pathSuffix);
        } else if (permalinkType.equals(PostPermalinkType.ID)) {
            fullPath.append("?p=")
                .append(post.getId());
        } else if (permalinkType.equals(PostPermalinkType.DATE)) {
            fullPath.append(DateUtils.year(post.getCreateTime()))
                .append(URL_SEPARATOR)
                .append(monthString)
                .append(URL_SEPARATOR)
                .append(post.getSlug())
                .append(pathSuffix);
        } else if (permalinkType.equals(PostPermalinkType.DAY)) {
            fullPath.append(DateUtils.year(post.getCreateTime()))
                .append(URL_SEPARATOR)
                .append(monthString)
                .append(URL_SEPARATOR)
                .append(dayString)
                .append(URL_SEPARATOR)
                .append(post.getSlug())
                .append(pathSuffix);
        } else if (permalinkType.equals(PostPermalinkType.YEAR)) {
            fullPath.append(DateUtils.year(post.getCreateTime()))
                .append(URL_SEPARATOR)
                .append(post.getSlug())
                .append(pathSuffix);
        } else if (permalinkType.equals(PostPermalinkType.ID_SLUG)) {
            fullPath.append(archivesPrefix)
                .append(URL_SEPARATOR)
                .append(post.getId())
                .append(pathSuffix);
        }

        post.setFullPath(fullPath.toString());

        return post;
    }

    @Override
    public void validateTarget(@NonNull Integer postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new NotFoundException("查询不到该文章的信息").setErrorData(postId));

        if (post.getDisallowComment()) {
            throw new BadRequestException("该文章已经被禁止评论").setErrorData(postId);
        }
    }

    @Override
    public void validateCommentBlackListStatus() {
        CommentViolationTypeEnum banStatus =
            commentBlackListService.commentsBanStatus(ServletUtils.getRequestIp());
        Integer banTime = optionService
            .getByPropertyOrDefault(CommentProperties.COMMENT_BAN_TIME, Integer.class, 10);
        if (banStatus == CommentViolationTypeEnum.FREQUENTLY) {
            throw new ForbiddenException(String.format("您的评论过于频繁，请%s分钟之后再试。", banTime));
        }
    }

}
