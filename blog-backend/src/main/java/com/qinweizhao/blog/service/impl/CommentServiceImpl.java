package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.convert.CommentConvert;
import com.qinweizhao.blog.mapper.CommentMapper;
import com.qinweizhao.blog.mapper.PostMapper;
import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.entity.Comment;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.model.projection.CommentCountProjection;
import com.qinweizhao.blog.service.CommentService;
import com.qinweizhao.blog.service.PostService;
import com.qinweizhao.blog.util.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

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


    private final PostService postService;

    private final PostMapper postMapper;
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
    public PageResult<CommentDTO> pageComment(CommentQueryParam commentQueryParam) {
        PageResult<Comment> commentResult = this.baseMapper.selectPageComments(commentQueryParam);
        return CommentConvert.INSTANCE.convertToDTO(commentResult);

    }

    @Override
    public Map<Integer, Long> countByPostIds(Set<Integer> postIds) {
        List<CommentCountProjection> commentCountProjections = this.baseMapper.selectCountByPostIds(postIds);
        return ServiceUtils.convertToMap(commentCountProjections, CommentCountProjection::getPostId, CommentCountProjection::getCount);
    }


}
