package com.qinweizhao.blog.controller.admin.api;

import cn.hutool.core.date.DateUtil;
import com.qinweizhao.blog.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.convert.CategoryConvert;
import com.qinweizhao.blog.convert.MetaConvert;
import com.qinweizhao.blog.convert.PostConvert;
import com.qinweizhao.blog.convert.TagConvert;
import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.post.BasePostSimpleDTO;
import com.qinweizhao.blog.model.entity.Category;
import com.qinweizhao.blog.model.entity.Meta;
import com.qinweizhao.blog.model.entity.Tag;
import com.qinweizhao.blog.model.enums.PostPermalinkType;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.model.vo.PostListVO;
import com.qinweizhao.blog.service.*;
import com.qinweizhao.blog.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static com.qinweizhao.blog.model.support.HaloConst.URL_SEPARATOR;

/**
 * Post controller.
 *
 * @author johnniang
 * @author ryanwang
 * @author guqing
 * @author qinweizhao
 * @date 2019-03-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/posts")
public class PostController {

    private final PostService postService;

    private final PostTagService postTagService;

    private final PostCategoryService postCategoryService;

    private final CommentService commentService;

    private final MetaService metaService;

    private final AbstractStringCacheStore cacheStore;

    private final OptionService optionService;


    /**
     * 分页
     *
     * @param postQueryParam postQueryParam
     * @param more           more
     * @return PageResult
     */
    @GetMapping
    public PageResult<? extends BasePostSimpleDTO> pageBy(PostQueryParam postQueryParam, @RequestParam(value = "more", defaultValue = "true") Boolean more) {
        PageResult<BasePostSimpleDTO> postPage = postService.pagePosts(postQueryParam);
        if (more) {
            return this.buildPostListVO(postPage);
        }

        return postPage;
    }

    private PageResult<PostListVO> buildPostListVO(PageResult<BasePostSimpleDTO> postPage) {

        List<BasePostSimpleDTO> posts = postPage.getContent();

        Set<Integer> postIds = ServiceUtils.fetchProperty(posts, BasePostSimpleDTO::getId);

        // Get tag list map
        Map<Integer, List<Tag>> tagListMap = postTagService.listTagListMapBy(postIds);

        // Get category list map
        Map<Integer, List<Category>> categoryListMap = postCategoryService
                .listCategoryListMap(postIds);

        // Get comment count
        Map<Integer, Long> commentCountMap = commentService.countByPostIds(postIds);

        // Get post meta list map
        Map<Integer, List<Meta>> postMetaListMap = metaService.getListMetaAsMapByPostIds(postIds);


        List<PostListVO> collect = posts.stream().map(post -> {

            PostListVO postListVO = PostConvert.INSTANCE.convertToListVO(post);


//            if (StringUtils.isBlank(postListVO.getSummary())) {
//                postListVO.setSummary(generateSummary(post.getFormatContent()));
//            }

            Optional.ofNullable(tagListMap.get(post.getId())).orElseGet(LinkedList::new);

            // Set tags
            postListVO.setTags(Optional.ofNullable(tagListMap.get(post.getId()))
                    .orElseGet(LinkedList::new)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(TagConvert.INSTANCE::convert)
                    .collect(Collectors.toList()));

            // Set categories
            postListVO.setCategories(Optional.ofNullable(categoryListMap.get(post.getId()))
                    .orElseGet(LinkedList::new)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(CategoryConvert.INSTANCE::convertVO)
//                    .map(categoryService::convertTo)
                    .collect(Collectors.toList()));

            // Set post metas
            List<Meta> metas = Optional.ofNullable(postMetaListMap.get(post.getId()))
                    .orElseGet(LinkedList::new);
            postListVO.setMetas(MetaConvert.INSTANCE.convertToMap(metas));

            // Set comment count
            postListVO.setCommentCount(commentCountMap.getOrDefault(post.getId(), 0L));

            postListVO.setFullPath(buildFullPath(post));

            return postListVO;
        }).collect(Collectors.toList());
        return new PageResult<>(collect, collect.size());
    }


    private String buildFullPath(BasePostSimpleDTO post) {

        PostPermalinkType permalinkType = optionService.getPostPermalinkType();

        String pathSuffix = optionService.getPathSuffix();

        String archivesPrefix = optionService.getArchivesPrefix();

        int month = DateUtil.month(post.getCreateTime()) + 1;

        String monthString = month < 10 ? "0" + month : String.valueOf(month);

        int day = DateUtil.dayOfMonth(post.getCreateTime());

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
            fullPath.append(DateUtil.year(post.getCreateTime()))
                    .append(URL_SEPARATOR)
                    .append(monthString)
                    .append(URL_SEPARATOR)
                    .append(post.getSlug())
                    .append(pathSuffix);
        } else if (permalinkType.equals(PostPermalinkType.DAY)) {
            fullPath.append(DateUtil.year(post.getCreateTime()))
                    .append(URL_SEPARATOR)
                    .append(monthString)
                    .append(URL_SEPARATOR)
                    .append(dayString)
                    .append(URL_SEPARATOR)
                    .append(post.getSlug())
                    .append(pathSuffix);
        } else if (permalinkType.equals(PostPermalinkType.YEAR)) {
            fullPath.append(DateUtil.year(post.getCreateTime()))
                    .append(URL_SEPARATOR)
                    .append(post.getSlug())
                    .append(pathSuffix);
        }
        return fullPath.toString();
    }

//    @GetMapping("latest")
//    @ApiOperation("Pages latest post")
//    public List<BasePostMinimalDTO> pageLatest(@RequestParam(name = "top", defaultValue = "10") int top) {
//        return postService.convertToMinimal(postService.pageLatest(top).getContent());
//    }

//    @GetMapping("status/{status}")
//    @ApiOperation("Gets a page of post by post status")
//    public Page<? extends BasePostSimpleDTO> pageByStatus(@PathVariable(name = "status") PostStatus status,
//                                                          @RequestParam(value = "more", required = false, defaultValue = "false") Boolean more,
//                                                          @PageableDefault(sort = "createTime", direction = DESC) Pageable pageable) {
//        Page<Post> posts = postService.pageBy(status, pageable);
//
//        if (more) {
//            return postService.convertToListVo(posts);
//        }
//
//        return postService.convertToSimple(posts);
//    }
//
//    @GetMapping("{postId:\\d+}")
//    @ApiOperation("Gets a post")
//    public PostDetailVO getBy(@PathVariable("postId") Integer postId) {
//        Post post = postService.getById(postId);
//        return postService.convertToDetailVo(post);
//    }
//
//    @PutMapping("{postId:\\d+}/likes")
//    @ApiOperation("Likes a post")
//    public void likes(@PathVariable("postId") Integer postId) {
//        postService.increaseLike(postId);
//    }
//
//    @PostMapping
//    @ApiOperation("Creates a post")
//    public PostDetailVO createBy(@Valid @RequestBody PostParam postParam,
//                                 @RequestParam(value = "autoSave", required = false, defaultValue = "false") Boolean autoSave) {
//        // Convert to
//        Post post = postParam.convertTo();
//        return postService.createBy(post, postParam.getTagIds(), postParam.getCategoryIds(), postParam.getPostMetas(), autoSave);
//    }
//
//    @PutMapping("{postId:\\d+}")
//    @ApiOperation("Updates a post")
//    public PostDetailVO updateBy(@Valid @RequestBody PostParam postParam,
//                                 @PathVariable("postId") Integer postId,
//                                 @RequestParam(value = "autoSave", required = false, defaultValue = "false") Boolean autoSave) {
//        // Get the post info
//        Post postToUpdate = postService.getById(postId);
//
//        postParam.update(postToUpdate);
//        return postService.updateBy(postToUpdate, postParam.getTagIds(), postParam.getCategoryIds(), postParam.getPostMetas(), autoSave);
//    }
//
//    @PutMapping("{postId:\\d+}/status/{status}")
//    @ApiOperation("Updates post status")
//    public BasePostMinimalDTO updateStatusBy(
//            @PathVariable("postId") Integer postId,
//            @PathVariable("status") PostStatus status) {
//        Post post = postService.updateStatus(status, postId);
//
//        return new BasePostMinimalDTO().convertFrom(post);
//    }
//
//    @PutMapping("status/{status}")
//    @ApiOperation("Updates post status in batch")
//    public List<Post> updateStatusInBatch(@PathVariable(name = "status") PostStatus status,
//                                          @RequestBody List<Integer> ids) {
//        return postService.updateStatusByIds(ids, status);
//    }
//
//    @PutMapping("{postId:\\d+}/status/draft/content")
//    @ApiOperation("Updates draft")
//    public BasePostDetailDTO updateDraftBy(
//            @PathVariable("postId") Integer postId,
//            @RequestBody PostContentParam contentParam) {
//        // Update draft content
//        Post post = postService.updateDraftContent(contentParam.getContent(), postId);
//
//        return new BasePostDetailDTO().convertFrom(post);
//    }
//
//    @DeleteMapping("{postId:\\d+}")
//    @ApiOperation("Deletes a photo permanently")
//    public void deletePermanently(@PathVariable("postId") Integer postId) {
//        postService.removeById(postId);
//    }
//
//    @DeleteMapping
//    @ApiOperation("Deletes posts permanently in batch by id array")
//    public List<Post> deletePermanentlyInBatch(@RequestBody List<Integer> ids) {
//        return postService.removeByIds(ids);
//    }
//
//    @GetMapping(value = {"preview/{postId:\\d+}", "{postId:\\d+}/preview"})
//    @ApiOperation("Gets a post preview link")
//    public String preview(@PathVariable("postId") Integer postId) throws UnsupportedEncodingException {
//        Post post = postService.getById(postId);
//
//        post.setSlug(URLEncoder.encode(post.getSlug(), StandardCharsets.UTF_8.name()));
//
//        BasePostMinimalDTO postMinimalDTO = postService.convertToMinimal(post);
//
//        String token = IdUtil.simpleUUID();
//
//        // cache preview token
//        cacheStore.putAny(token, token, 10, TimeUnit.MINUTES);
//
//        StringBuilder previewUrl = new StringBuilder();
//
//        if (!optionService.isEnabledAbsolutePath()) {
//            previewUrl.append(optionService.getBlogBaseUrl());
//        }
//
//        previewUrl.append(postMinimalDTO.getFullPath());
//
//        if (optionService.getPostPermalinkType().equals(PostPermalinkType.ID)) {
//            previewUrl.append("&token=")
//                    .append(token);
//        } else {
//            previewUrl.append("?token=")
//                    .append(token);
//        }
//
//        // build preview post url and return
//        return previewUrl.toString();
//    }
}
