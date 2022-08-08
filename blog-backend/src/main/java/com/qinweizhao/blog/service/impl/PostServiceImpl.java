package com.qinweizhao.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.qinweizhao.blog.model.convert.MetaConvert;
import com.qinweizhao.blog.model.convert.PostConvert;
import com.qinweizhao.blog.exception.AlreadyExistsException;
import com.qinweizhao.blog.exception.ServiceException;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.mapper.*;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.*;
import com.qinweizhao.blog.model.entity.Content;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.entity.PostCategory;
import com.qinweizhao.blog.model.entity.PostTag;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.MetaParam;
import com.qinweizhao.blog.model.param.PostParam;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.model.properties.PostProperties;
import com.qinweizhao.blog.model.vo.ArchiveMonthVO;
import com.qinweizhao.blog.model.vo.ArchiveYearVO;
import com.qinweizhao.blog.service.*;
import com.qinweizhao.blog.util.HaloUtils;
import com.qinweizhao.blog.util.MarkdownUtils;
import com.qinweizhao.blog.util.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.qinweizhao.blog.model.support.HaloConst.URL_SEPARATOR;

/**
 * Post service implementation.
 *
 * @author johnniang
 * @author ryanwang
 * @author guqing
 * @author evanwang
 * @author coor.top
 * @author qinweizhao
 * @date 2019-03-14
 */
@Slf4j
@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final ContentMapper contentMapper;

    private final Pattern summaryPattern = Pattern.compile("\t|\r|\n");

    private final PostMapper postMapper;

    private final OptionService optionService;

    private final PostTagMapper postTagMapper;

    private final PostTagService postTagService;

    private final CommentMapper commentMapper;

    private final CommentService commentService;

    private final PostCategoryMapper postCategoryMapper;

    private final PostCategoryService postCategoryService;

    private final MetaService metaService;

    private AbstractStringCacheStore cacheStore;

    @Override
    public PageResult<PostListDTO> page(PostQueryParam param) {

        PageResult<Post> pageResult = postMapper.selectPage(param);
        List<Post> pageContent = pageResult.getContent();
        List<PostSimpleDTO> posts = PostConvert.INSTANCE.convertToSimpleDTO(pageContent);

        Set<Integer> postIds = ServiceUtils.fetchProperty(posts, PostSimpleDTO::getId);


        Map<Integer, List<TagDTO>> tagListMap = postTagService.listTagListMapBy(postIds);
        Map<Integer, List<CategoryDTO>> categoryListMap = postCategoryService.listCategoryListMap(postIds);
        Map<Integer, List<MetaDTO>> postMetaListMap = metaService.getListMetaAsMapByPostIds(postIds);
        Map<Integer, Long> commentCountMap = commentService.countByPostIds(postIds);


        List<PostListDTO> collect = posts.stream().map(post -> {
            PostListDTO postListDTO = PostConvert.INSTANCE.convertToListVO(post);

            postListDTO.setTags(new ArrayList<>(
                    Optional.ofNullable(tagListMap.get(post.getId()))
                            .orElseGet(LinkedList::new)));

            postListDTO.setCategories(new ArrayList<>(
                    Optional.ofNullable(categoryListMap.get(post.getId()))
                            .orElseGet(LinkedList::new)));

            postListDTO.setMetas(
                    Optional.ofNullable(postMetaListMap.get(post.getId()))
                            .orElseGet(LinkedList::new));

            postListDTO.setCommentCount(commentCountMap.getOrDefault(post.getId(), 0L));

            postListDTO.setFullPath(buildFullPath(post.getId()));

            return postListDTO;
        }).collect(Collectors.toList());

        return new PageResult<>(collect, pageResult.getTotal(), pageResult.hasPrevious(), pageResult.hasNext());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(PostParam param) {
        Post post = PostConvert.INSTANCE.convert(param);
        this.slugMustNotExist(post);

        Set<Integer> categoryIds = param.getCategoryIds();
        Set<Integer> tagIds = param.getTagIds();
        Set<MetaParam> metas = param.getMetas();

        String originalContent = param.getOriginalContent();

        if (StringUtils.isNotEmpty(post.getPassword()) && param.getStatus() != PostStatus.DRAFT) {
            post.setStatus(PostStatus.INTIMATE.getValue());
        }

        originalContent = HaloUtils.cleanHtmlTag(originalContent);
        post.setWordCount((long) originalContent.length());
        postMapper.insert(post);
        Integer postId = post.getId();

        Content content = new Content();
        content.setOriginalContent(originalContent);
        content.setContent(MarkdownUtils.renderHtml(originalContent));
        content.setPostId(postId);
        contentMapper.insert(content);

        this.savePostRelation(categoryIds, tagIds, metas, postId);

        return true;
    }

    /**
     * 保存文章和分类/标签/元数据 关联关系
     *
     * @param categoryIds categoryIds
     * @param tagIds      tagIds
     * @param metas       metas
     * @param postId      postId
     */
    private void savePostRelation(Collection<Integer> categoryIds, Collection<Integer> tagIds, Set<MetaParam> metas, Integer postId) {
        if (!CollectionUtils.isEmpty(categoryIds)) {
            // 保存 文章-分类 关联
            List<PostCategory> postCategories = categoryIds.stream().map(categoryId -> {
                PostCategory postCategory = new PostCategory();
                postCategory.setPostId(postId);
                postCategory.setCategoryId(categoryId);
                return postCategory;
            }).collect(Collectors.toList());
            postCategoryService.saveBatch(postCategories);
        }

        if (!CollectionUtils.isEmpty(tagIds)) {
            // 保存 文章-标签 关联
            List<PostTag> postTags = tagIds.stream().map(tagId -> {
                PostTag postTag = new PostTag();
                postTag.setPostId(postId);
                postTag.setTagId(tagId);
                return postTag;
            }).collect(Collectors.toList());
            postTagService.saveBatch(postTags);
        }

        if (!CollectionUtils.isEmpty(metas)) {
            metas.forEach(item -> item.setPostId(postId));
            // 保存 文章元数据 关联
            metaService.saveBatch(MetaConvert.INSTANCE.convert(metas));
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Integer postId, PostParam param) {
        Post post = PostConvert.INSTANCE.convert(param);
        post.setId(postId);

        this.slugMustNotExist(post);

        postMapper.updateById(post);

        // 标签
        Set<Integer> tagIds = param.getTagIds();
        Set<Integer> dbTagIds = postTagMapper.selectTagIdsByPostId(postId);
        Collection<Integer> addTagIds = CollUtil.subtract(tagIds, dbTagIds);
        Collection<Integer> removeTagIds = CollUtil.subtract(dbTagIds, tagIds);
        if (!CollectionUtils.isEmpty(removeTagIds)) {
            postTagMapper.deleteBatchByPostIdAndTagIds(postId, removeTagIds);
        }

        // 分类
        Set<Integer> categoryIds = param.getCategoryIds();
        Set<Integer> dbCategoryIds = postCategoryMapper.selectSetCategoryIdsByPostId(postId);
        Collection<Integer> addCategoryIds = CollUtil.subtract(categoryIds, dbCategoryIds);
        Collection<Integer> removeCategoryIds = CollUtil.subtract(dbCategoryIds, categoryIds);
        if (!CollectionUtils.isEmpty(removeCategoryIds)) {
            postCategoryMapper.deleteBatchByPostIdAndTagIds(postId, removeCategoryIds);
        }

        // 元数据
        Set<MetaParam> metas = param.getMetas();
        // 删除所有元数据
        metaService.removeByPostId(postId);

        this.savePostRelation(addCategoryIds, addTagIds, metas, postId);
        return false;
    }


    private void slugMustNotExist(Post post) {

        boolean exist;

        if (ServiceUtils.isEmptyId(post.getId())) {

            exist = postMapper.selectExistsBySlug(post.getSlug());
        } else {

            exist = !postMapper.selectExistsByIdNotAndSlug(post.getId(), post.getSlug());
        }

        if (exist) {
            throw new AlreadyExistsException("文章别名 " + post.getSlug() + " 已存在");
        }
    }


    @Override
    public long countByStatus(PostStatus published) {
        return postMapper.selectCountByStatus(published);
    }

    @Override
    public long countVisit() {
        return postMapper.selectCountVisits();
    }

    @Override
    public long countLike() {
        return postMapper.selectCountLikes();
    }

    @Override
    public String getPreviewUrl(Integer postId) {
        Post post = postMapper.selectById(postId);
        String token = IdUtil.simpleUUID();

        // cache preview token
        cacheStore.putAny(token, token, 10, TimeUnit.MINUTES);

        StringBuilder previewUrl = new StringBuilder();

        if (!optionService.isEnabledAbsolutePath()) {
            previewUrl.append(optionService.getBlogBaseUrl());
        }

        previewUrl.append(this.buildFullPath(postId));

        previewUrl.append("&token=")
                .append(token);
        // build preview post url and return
        return previewUrl.toString();
    }

    @Override
    public boolean removeById(Integer postId) {
        Assert.notNull(postId, "Post id must not be null");

        // 标签
        postTagMapper.deleteByPostId(postId);
        // 分类
        postCategoryMapper.deleteByPostId(postId);
        // 元数据
        metaService.removeByPostId(postId);
        // 评论
        commentMapper.deleteByPostId(postId);

        return postMapper.deleteById(postId) > 0;
    }

    @Override
    public boolean removeByIds(List<Integer> postIds) {

        if (CollectionUtils.isEmpty(postIds)) {
            return false;
        }

        postIds.forEach(this::removeById);

        return true;
    }

    @Override
    public boolean updateDraftContent(String newContent, Integer postId) {
        if (newContent == null) {
            newContent = "";
        }

        Content content = contentMapper.selectById(postId);


        if (StringUtils.equals(newContent, content.getOriginalContent())) {
            return true;
        }
        Content updateContent = new Content();
        updateContent.setOriginalContent(newContent);
        updateContent.setPostId(postId);
        return contentMapper.updateById(updateContent) != 1;
    }


    @Override
    public boolean updateStatus(PostStatus status, Integer postId) {
        // Get post
        Post post = postMapper.selectById(postId);

        if (!(status.getValue().equals(post.getStatus()))) {

            int updatedRows = postMapper.updateStatusById(status.getValue(), postId);
            if (updatedRows != 1) {
                throw new ServiceException("无法更新状态,id ： " + postId);
            }
        }

        // 同步内容
        if (PostStatus.PUBLISHED.equals(status)) {
            // 如果发布此帖子，则转换格式化内容
            Content content = contentMapper.selectById(postId);
            String formatContent = MarkdownUtils.renderHtml(content.getContent());

            Content updateContent = new Content();
            updateContent.setPostId(postId);
            updateContent.setContent(formatContent);
            int updatedRows = contentMapper.updateById(updateContent);

            if (updatedRows != 1) {
                throw new ServiceException("无法更新帖子格式内容，id: " + postId);
            }
        }

        return true;
    }

    @Override
    public boolean updateStatusByIds(List<Integer> ids, PostStatus status) {
        ids.forEach(item -> this.updateStatus(status, item));
        return true;
    }

    @Override
    public PostDTO getById(Integer postId) {

        Post post = postMapper.selectById(postId);

        PostDTO postDTO = PostConvert.INSTANCE.convert(post);

        List<TagDTO> tags = postTagService.listTagsByPostId(post.getId());

        List<CategoryDTO> categories = postCategoryService.listCategoriesByPostId(post.getId());

        Content content = contentMapper.selectById(postId);
        postDTO.setFormatContent(content.getContent());
        postDTO.setOriginalContent(content.getOriginalContent());
        postDTO.setSummary(generateSummary(content.getContent()));

        Set<Integer> tagIds = ServiceUtils.fetchProperty(tags, TagDTO::getId);

        Set<Integer> categoryIds = ServiceUtils.fetchProperty(categories, CategoryDTO::getId);

        postDTO.setTagIds(tagIds);
        postDTO.setTags(tags);

        postDTO.setCategoryIds(categoryIds);
        postDTO.setCategories(categories);

        postDTO.setFullPath(buildFullPath(post.getId()));

        return postDTO;
    }


    @Override
    public PostDTO getBySlugAndStatus(PostStatus published, String slug) {
        Post post = postMapper.selectBySlugAndStatus(slug, published);
        return PostConvert.INSTANCE.convert(post);
    }

    @Override
    public String generateDescription(String content) {

        String text = HaloUtils.cleanHtmlTag(content);

        Matcher matcher = summaryPattern.matcher(text);
        text = matcher.replaceAll("");

        // Get summary length
        Integer summaryLength = optionService.getByPropertyOrDefault(PostProperties.SUMMARY_LENGTH, Integer.class, 150);

        return StringUtils.substring(text, 0, summaryLength);
    }

    @Override
    public List<PostListDTO> convertToListVo(List<PostSimpleDTO> posts) {
        Assert.notNull(posts, "Post page must not be null");

        Set<Integer> postIds = ServiceUtils.fetchProperty(posts, PostSimpleDTO::getId);

        // Get tag list map
        Map<Integer, List<TagDTO>> tagListMap = postTagService.listTagListMapBy(postIds);

        // Get category list map
        Map<Integer, List<CategoryDTO>> categoryListMap = postCategoryService
                .listCategoryListMap(postIds);

        // Get comment count
        Map<Integer, Long> commentCountMap = commentService.countByPostIds(postIds);

        // Get post meta list map
//        Map<Integer, List<PostMeta>> postMetaListMap = postMetaService.listPostMetaAsMap(postIds);

        // todo
        // is null
        return posts.stream().map(post -> {
            PostListDTO postListDTO = PostConvert.INSTANCE.convertToListVO(post);
//            if (StringUtils.isBlank(postListDTO.getSummary())) {
//                postListDTO.setSummary(generateSummary(post.getFormatContent()));
//            }

            Optional.ofNullable(tagListMap.get(post.getId())).orElseGet(LinkedList::new);

            // Set tags
            postListDTO.setTags(Optional.ofNullable(tagListMap.get(post.getId()))
                    .orElseGet(LinkedList::new)
                    .stream()

                    .collect(Collectors.toList()));

            // Set categories
            postListDTO.setCategories(Optional.ofNullable(categoryListMap.get(post.getId()))
                    .orElseGet(LinkedList::new)
                    .stream()
                    .collect(Collectors.toList()));

            // Set post metas
//            List<PostMeta> metas = Optional.ofNullable(postMetaListMap.get(post.getId()))
//                    .orElseGet(LinkedList::new);
//            postListVO.setMetas(postMetaService.convertToMap(metas));

            // Set comment count
            postListDTO.setCommentCount(commentCountMap.getOrDefault(post.getId(), 0L));

//            postListDTO.setFullPath(buildFullPath(post));

            return postListDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ArchiveYearVO> listYearArchives() {
        // Get all posts
        List<Post> posts = postMapper
                .selectListByStatus(PostStatus.PUBLISHED);

        return convertToYearArchives(posts);
    }

    private List<ArchiveYearVO> convertToYearArchives(List<Post> posts) {
        Map<Integer, List<PostSimpleDTO>> yearPostMap = new HashMap<>(8);

//        posts.forEach(post -> {
//            LocalDateTime createTime = post.getCreateTime();
//            yearPostMap.computeIfAbsent(createTime.getYear(), year -> new LinkedList<>())
//                    .add(PostConvert.INSTANCE.convert(post));
//        });

        List<ArchiveYearVO> archives = new LinkedList<>();

        yearPostMap.forEach((year, postList) -> {
            // Build archive
            ArchiveYearVO archive = new ArchiveYearVO();
            archive.setYear(year);
            archive.setPosts(convertToListVo(postList));

            // Add archive
            archives.add(archive);
        });

        // Sort this list
        archives.sort(new ArchiveYearVO.ArchiveComparator());

        return archives;

    }

    @Override
    public List<ArchiveMonthVO> listMonthArchives() {
        // Get all posts
        List<Post> posts = postMapper
                .selectListByStatus(PostStatus.PUBLISHED);

        return convertToMonthArchives(posts);
    }

    @Override
    public List<PostSimpleDTO> listSimple(int top) {
        List<Post> posts = postMapper.selectListLatest(top);
        return PostConvert.INSTANCE.convertToSimpleDTO(posts);
    }


    private List<ArchiveMonthVO> convertToMonthArchives(List<Post> posts) {


        Map<Integer, Map<Integer, List<PostSimpleDTO>>> yearMonthPostMap = new HashMap<>(8);

//        posts.forEach(post -> {
//            LocalDateTime createTime = post.getCreateTime();
//            yearMonthPostMap.computeIfAbsent(createTime.getYear(), year -> new HashMap<>())
//                    .computeIfAbsent(createTime.getMonthValue() + 1,
//                            month -> new LinkedList<>())
////                    .add(PostConvert.INSTANCE.convert(post));
//        });

        List<ArchiveMonthVO> archives = new LinkedList<>();

        yearMonthPostMap.forEach((year, monthPostMap) ->
                monthPostMap.forEach((month, postList) -> {
                    ArchiveMonthVO archive = new ArchiveMonthVO();
                    archive.setYear(year);
                    archive.setMonth(month);
                    archive.setPosts(convertToListVo(postList));

                    archives.add(archive);
                }));

        // Sort this list
        archives.sort(new ArchiveMonthVO.ArchiveComparator());

        return archives;
    }

    protected String generateSummary(@NonNull String htmlContent) {
        Assert.notNull(htmlContent, "文章原始内容不能为空");

        String text = HaloUtils.cleanHtmlTag(htmlContent);

        Matcher matcher = summaryPattern.matcher(text);
        text = matcher.replaceAll("");

        Integer summaryLength = optionService.getByPropertyOrDefault(PostProperties.SUMMARY_LENGTH, Integer.class, 150);

        return StringUtils.substring(text, 0, summaryLength);
    }


    @Override
    public String buildFullPath(Integer postId) {
        StringBuilder fullPath = new StringBuilder();

        if (optionService.isEnabledAbsolutePath()) {
            fullPath.append(optionService.getBlogBaseUrl());
        }

        fullPath.append(URL_SEPARATOR);

        fullPath.append("?p=")
                .append(postId);

        return fullPath.toString();
    }

}
