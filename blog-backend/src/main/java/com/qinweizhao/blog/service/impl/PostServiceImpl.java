package com.qinweizhao.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.qinweizhao.blog.convert.MetaConvert;
import com.qinweizhao.blog.convert.PostConvert;
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

        PageResult<Post> pageResult = postMapper.selectPagePosts(param);
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
        // 保存 文章-分类 关联
        List<PostCategory> postCategories = categoryIds.stream().map(categoryId -> {
            PostCategory postCategory = new PostCategory();
            postCategory.setPostId(postId);
            postCategory.setCategoryId(categoryId);
            return postCategory;
        }).collect(Collectors.toList());
        postCategoryService.saveBatch(postCategories);

        // 保存 文章-标签 关联
        List<PostTag> postTags = tagIds.stream().map(tagId -> {
            PostTag postTag = new PostTag();
            postTag.setPostId(postId);
            postTag.setTagId(tagId);
            return postTag;
        }).collect(Collectors.toList());
        postTagService.saveBatch(postTags);

        // 保存 文章元数据 关联
        metaService.saveBatch(MetaConvert.INSTANCE.convert(metas));
    }

    @Override
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
        postTagService.removeBatchByIds(removeTagIds);


        // 分类
        Set<Integer> categoryIds = param.getCategoryIds();
        Set<Integer> dbCategoryIds = postCategoryMapper.selectSetCategoryIdsByPostId(postId);
        Collection<Integer> addCategoryIds = CollUtil.subtract(categoryIds, dbCategoryIds);
        Collection<Integer> removeCategoryIds = CollUtil.subtract(dbCategoryIds, categoryIds);
        postCategoryService.removeBatchByIds(removeCategoryIds);

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
            // The sheet will be updated
            exist = postMapper.selectExistsByIdNotAndSlug(post.getId(), post.getSlug());
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
            int updatedRows = postMapper.updateFormatContent(formatContent, postId);

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
//
//        posts.forEach(post -> {
//            LocalDateTime createTime = post.getCreateTime();
//            yearPostMap.computeIfAbsent(createTime.getYear(), year -> new LinkedList<>())
////                    .add(PostConvert.INSTANCE.convert(post));
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
        Assert.notNull(htmlContent, "html content must not be null");

        String text = HaloUtils.cleanHtmlTag(htmlContent);

        Matcher matcher = summaryPattern.matcher(text);
        text = matcher.replaceAll("");

        // Get summary length
        Integer summaryLength = optionService.getByPropertyOrDefault(PostProperties.SUMMARY_LENGTH, Integer.class, 150);

        return StringUtils.substring(text, 0, summaryLength);
    }

    /**
     * 构建完整路径
     *
     * @param postId postId
     * @return String
     */
    private String buildFullPath(Integer postId) {
        StringBuilder fullPath = new StringBuilder();

        if (optionService.isEnabledAbsolutePath()) {
            fullPath.append(optionService.getBlogBaseUrl());
        }

        fullPath.append(URL_SEPARATOR);

        fullPath.append("?p=")
                .append(postId);

        return fullPath.toString();
    }


//    @Override
//    public Post getBy(Integer year, Integer month, String slug) {
//        Assert.notNull(year, "Post create year must not be null");
//        Assert.notNull(month, "Post create month must not be null");
//        Assert.notNull(slug, "Post slug must not be null");
//
//        Optional<Post> postOptional = postRepository.findBy(year, month, slug);
//
//        return postOptional
//                .orElseThrow(() -> new NotFoundException("查询不到该文章的信息").setErrorData(slug));
//    }
//
//    @NonNull
//    @Override
//    public Post getBy(@NonNull Integer year, @NonNull String slug) {
//        Assert.notNull(year, "Post create year must not be null");
//        Assert.notNull(slug, "Post slug must not be null");
//
//        Optional<Post> postOptional = postRepository.findBy(year, slug);
//
//        return postOptional
//                .orElseThrow(() -> new NotFoundException("查询不到该文章的信息").setErrorData(slug));
//    }
//
//    @Override
//    public Post getBy(Integer year, Integer month, String slug, PostStatus status) {
//        Assert.notNull(year, "Post create year must not be null");
//        Assert.notNull(month, "Post create month must not be null");
//        Assert.notNull(slug, "Post slug must not be null");
//        Assert.notNull(status, "Post status must not be null");
//
//        Optional<Post> postOptional = postRepository.findBy(year, month, slug, status);
//
//        return postOptional
//                .orElseThrow(() -> new NotFoundException("查询不到该文章的信息").setErrorData(slug));
//    }
//
//    @Override
//    public Post getBy(Integer year, Integer month, Integer day, String slug) {
//        Assert.notNull(year, "Post create year must not be null");
//        Assert.notNull(month, "Post create month must not be null");
//        Assert.notNull(day, "Post create day must not be null");
//        Assert.notNull(slug, "Post slug must not be null");
//
//        Optional<Post> postOptional = postRepository.findBy(year, month, day, slug);
//
//        return postOptional
//                .orElseThrow(() -> new NotFoundException("查询不到该文章的信息").setErrorData(slug));
//    }
//
//    @Override
//    public Post getBy(Integer year, Integer month, Integer day, String slug, PostStatus status) {
//        Assert.notNull(year, "Post create year must not be null");
//        Assert.notNull(month, "Post create month must not be null");
//        Assert.notNull(day, "Post create day must not be null");
//        Assert.notNull(slug, "Post slug must not be null");
//        Assert.notNull(status, "Post status must not be null");
//
//        Optional<Post> postOptional = postRepository.findBy(year, month, day, slug, status);
//
//        return postOptional
//                .orElseThrow(() -> new NotFoundException("查询不到该文章的信息").setErrorData(slug));
//    }
//
//    @Override
//    public List<Post> removeByIds(Collection<Integer> ids) {
//        if (CollectionUtils.isEmpty(ids)) {
//            return Collections.emptyList();
//        }
//        return ids.stream().map(this::removeById).collect(Collectors.toList());
//    }
//
//    @Override
//    public Post getBySlug(String slug) {
//        return postMapper.selectBySlug(slug);
//    }
//
//    @Override
//    public List<ArchiveYearVO> listYearArchives() {
//        // Get all posts
//        List<Post> posts = postRepository
//                .findAllByStatus(PostStatus.PUBLISHED, Sort.by(DESC, "createTime"));
//
//        return convertToYearArchives(posts);
//    }
//
//    @Override
//    public List<ArchiveMonthVO> listMonthArchives() {
//        // Get all posts
//        List<Post> posts = postRepository
//                .findAllByStatus(PostStatus.PUBLISHED, Sort.by(DESC, "createTime"));
//
//        return convertToMonthArchives(posts);
//    }
//
//    @Override
//    public List<ArchiveYearVO> convertToYearArchives(List<Post> posts) {
//        Map<Integer, List<Post>> yearPostMap = new HashMap<>(8);
//
//        posts.forEach(post -> {
//            Calendar calendar = DateUtils.convertTo(post.getCreateTime());
//            yearPostMap.computeIfAbsent(calendar.get(Calendar.YEAR), year -> new LinkedList<>())
//                    .add(post);
//        });
//
//        List<ArchiveYearVO> archives = new LinkedList<>();
//
//        yearPostMap.forEach((year, postList) -> {
//            // Build archive
//            ArchiveYearVO archive = new ArchiveYearVO();
//            archive.setYear(year);
//            archive.setPosts(convertToListVo(postList));
//
//            // Add archive
//            archives.add(archive);
//        });
//
//        // Sort this list
//        archives.sort(new ArchiveYearVO.ArchiveComparator());
//
//        return archives;
//    }
//
//    @Override
//    public List<ArchiveMonthVO> convertToMonthArchives(List<Post> posts) {
//
//        Map<Integer, Map<Integer, List<Post>>> yearMonthPostMap = new HashMap<>(8);
//
//        posts.forEach(post -> {
//            Calendar calendar = DateUtils.convertTo(post.getCreateTime());
//
//            yearMonthPostMap.computeIfAbsent(calendar.get(Calendar.YEAR), year -> new HashMap<>())
//                    .computeIfAbsent(calendar.get(Calendar.MONTH) + 1,
//                            month -> new LinkedList<>())
//                    .add(post);
//        });
//
//        List<ArchiveMonthVO> archives = new LinkedList<>();
//
//        yearMonthPostMap.forEach((year, monthPostMap) ->
//                monthPostMap.forEach((month, postList) -> {
//                    ArchiveMonthVO archive = new ArchiveMonthVO();
//                    archive.setYear(year);
//                    archive.setMonth(month);
//                    archive.setPosts(convertToListVo(postList));
//
//                    archives.add(archive);
//                }));
//
//        // Sort this list
//        archives.sort(new ArchiveMonthVO.ArchiveComparator());
//
//        return archives;
//    }
//
//    @Override
//    public PostDetailVO importMarkdown(String markdown, String filename) {
//        Assert.notNull(markdown, "Markdown document must not be null");
//
//        // Gets frontMatter
//        Map<String, List<String>> frontMatter = MarkdownUtils.getFrontMatter(markdown);
//        // remove frontMatter
//        markdown = MarkdownUtils.removeFrontMatter(markdown);
//
//        PostParam post = new PostParam();
//        post.setStatus(null);
//
//        List<String> elementValue;
//
//        Set<Integer> tagIds = new HashSet<>();
//
//        Set<Integer> categoryIds = new HashSet<>();
//
//        if (frontMatter.size() > 0) {
//            for (String key : frontMatter.keySet()) {
//                elementValue = frontMatter.get(key);
//                for (String ele : elementValue) {
//                    ele = StrUtil.strip(ele, "[", "]");
//                    ele = StrUtil.strip(ele, "\"");
//                    if ("".equals(ele)) {
//                        continue;
//                    }
//                    switch (key) {
//                        case "title":
//                            post.setTitle(ele);
//                            break;
//                        case "date":
//                            post.setCreateTime(DateUtil.parse(ele));
//                            break;
//                        case "permalink":
//                            post.setSlug(ele);
//                            break;
//                        case "thumbnail":
//                            post.setThumbnail(ele);
//                            break;
//                        case "status":
//                            post.setStatus(PostStatus.valueOf(ele));
//                            break;
//                        case "comments":
//                            post.setDisallowComment(Boolean.parseBoolean(ele));
//                            break;
//                        case "tags":
//                            Tag tag = tagService.getByName(ele);
//                            if (null == tag) {
//                                tag = new Tag();
//                                tag.setName(ele);
//                                tag.setSlug(SlugUtils.slug(ele));
//                                tag = tagService.create(tag);
//                            }
//                            tagIds.add(tag.getId());
//                            break;
//                        case "categories":
//                            Category category = categoryService.getByName(ele);
//                            if (null == category) {
//                                category = new Category();
//                                category.setName(ele);
//                                category.setSlug(SlugUtils.slug(ele));
//                                category.setDescription(ele);
//                                category = categoryService.create(category);
//                            }
//                            categoryIds.add(category.getId());
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            }
//        }
//
//        if (null == post.getStatus()) {
//            post.setStatus(PostStatus.PUBLISHED);
//        }
//
//        if (StringUtils.isEmpty(post.getTitle())) {
//            post.setTitle(filename);
//        }
//
//        if (StringUtils.isEmpty(post.getSlug())) {
//            post.setSlug(SlugUtils.slug(post.getTitle()));
//        }
//
//        post.setOriginalContent(markdown);
//
//        return createBy(post.convertTo(), tagIds, categoryIds, false);
//    }
//
//    @Override
//    public String exportMarkdown(Integer id) {
//        Assert.notNull(id, "Post id must not be null");
//        Post post = getById(id);
//        return exportMarkdown(post);
//    }
//
//    @Override
//    public String exportMarkdown(Post post) {
//        Assert.notNull(post, "Post must not be null");
//
//        StringBuilder content = new StringBuilder("---\n");
//
//        content.append("type: ").append("post").append("\n");
//        content.append("title: ").append(post.getTitle()).append("\n");
//        content.append("permalink: ").append(post.getSlug()).append("\n");
//        content.append("thumbnail: ").append(post.getThumbnail()).append("\n");
//        content.append("status: ").append(post.getStatus()).append("\n");
//        content.append("date: ").append(post.getCreateTime()).append("\n");
//        content.append("updated: ").append(post.getEditTime()).append("\n");
//        content.append("comments: ").append(!post.getDisallowComment()).append("\n");
//
//        List<Tag> tags = postTagService.listTagsBy(post.getId());
//
//        if (tags.size() > 0) {
//            content.append("tags:").append("\n");
//            for (Tag tag : tags) {
//                content.append("  - ").append(tag.getName()).append("\n");
//            }
//        }
//
//        List<Category> categories = postCategoryService.listCategoriesBy(post.getId());
//
//        if (categories.size() > 0) {
//            content.append("categories:").append("\n");
//            for (Category category : categories) {
//                content.append("  - ").append(category.getName()).append("\n");
//            }
//        }
//
//        List<PostMeta> metas = postMetaService.listBy(post.getId());
//
//        if (metas.size() > 0) {
//            content.append("metas:").append("\n");
//            for (PostMeta postMeta : metas) {
//                content.append("  - ").append(postMeta.getKey()).append(" :  ")
//                        .append(postMeta.getValue()).append("\n");
//            }
//        }
//
//        content.append("---\n\n");
//        content.append(post.getOriginalContent());
//        return content.toString();
//    }
//
//    @Override
//    public PostDetailVO convertToDetailVo(Post post) {
//        // List tags
//        List<Tag> tags = postTagService.listTagsBy(post.getId());
//        // List categories
//        List<Category> categories = postCategoryService.listCategoriesBy(post.getId());
//        // List metas
//        List<PostMeta> metas = postMetaService.listBy(post.getId());
//        // Convert to detail vo
//        return convertTo(post, tags, categories, metas);
//    }
//
//    @Override
//    public Post removeById(Integer postId) {
//        Assert.notNull(postId, "Post id must not be null");
//
//        log.debug("Removing post: [{}]", postId);
//
//        // Remove post tags
//        List<PostTag> postTags = postTagService.removeByPostId(postId);
//
//        log.debug("Removed post tags: [{}]", postTags);
//
//        // Remove post categories
//        List<PostCategory> postCategories = postCategoryService.removeByPostId(postId);
//
//        log.debug("Removed post categories: [{}]", postCategories);
//
//        // Remove metas
//        List<PostMeta> metas = postMetaService.removeByPostId(postId);
//        log.debug("Removed post metas: [{}]", metas);
//
//        // Remove post comments
//        List<PostComment> postComments = commentService.removeByPostId(postId);
//        log.debug("Removed post comments: [{}]", postComments);
//
//        Post deletedPost = super.removeById(postId);
//
//        // Log it
//        eventPublisher.publishEvent(new LogEvent(this, postId.toString(), LogType.POST_DELETED,
//                deletedPost.getTitle()));
//
//        return deletedPost;
//    }
//


//    @Override
//    public Page<PostDetailVO> convertToDetailVo(Page<Post> postPage) {
//        Assert.notNull(postPage, "Post page must not be null");
//        return postPage.map(this::convertToDetailVo);
//    }
//
//    @Override
//    public BasePostMinimalDTO convertToMinimal(Post post) {
//        Assert.notNull(post, "Post must not be null");
//        BasePostMinimalDTO basePostMinimalDTO = new BasePostMinimalDTO().convertFrom(post);
//
//        basePostMinimalDTO.setFullPath(buildFullPath(post));
//
//        return basePostMinimalDTO;
//    }
//
//    @Override
//    public List<BasePostMinimalDTO> convertToMinimal(List<Post> posts) {
//        if (CollectionUtils.isEmpty(posts)) {
//            return Collections.emptyList();
//        }
//
//        return posts.stream()
//                .map(this::convertToMinimal)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public BasePostSimpleDTO convertToSimple(Post post) {
//        Assert.notNull(post, "Post must not be null");
//
//        BasePostSimpleDTO basePostSimpleDTO = new BasePostSimpleDTO().convertFrom(post);
//
//        // Set summary
//        if (StringUtils.isBlank(basePostSimpleDTO.getSummary())) {
//            basePostSimpleDTO.setSummary(generateSummary(post.getFormatContent()));
//        }
//
//        basePostSimpleDTO.setFullPath(buildFullPath(post));
//
//        return basePostSimpleDTO;
//    }
//
//    /**
//     * Converts to post detail vo.
//     *
//     * @param post         post must not be null
//     * @param tags         tags
//     * @param categories   categories
//     * @param postMetaList postMetaList
//     * @return post detail vo
//     */
//    @NonNull
//    private PostDetailVO convertTo(@NonNull Post post, @Nullable List<Tag> tags,
//            @Nullable List<Category> categories, List<PostMeta> postMetaList) {
//        Assert.notNull(post, "Post must not be null");
//
//        // Convert to base detail vo
//        PostDetailVO postDetailVO = new PostDetailVO().convertFrom(post);
//
//        if (StringUtils.isBlank(postDetailVO.getSummary())) {
//            postDetailVO.setSummary(generateSummary(post.getFormatContent()));
//        }
//
//        // Extract ids
//        Set<Integer> tagIds = ServiceUtils.fetchProperty(tags, Tag::getId);
//        Set<Integer> categoryIds = ServiceUtils.fetchProperty(categories, Category::getId);
//        Set<Long> metaIds = ServiceUtils.fetchProperty(postMetaList, PostMeta::getId);
//
//        // Get post tag ids
//        postDetailVO.setTagIds(tagIds);
//        postDetailVO.setTags(tagService.convertTo(tags));
//
//        // Get post category ids
//        postDetailVO.setCategoryIds(categoryIds);
//        postDetailVO.setCategories(categoryService.convertTo(categories));
//
//        // Get post meta ids
//        postDetailVO.setMetaIds(metaIds);
//        postDetailVO.setMetas(postMetaService.convertTo(postMetaList));
//
//        postDetailVO.setCommentCount(commentService.countByPostId(post.getId()));
//
//        postDetailVO.setFullPath(buildFullPath(post));
//
//        return postDetailVO;
//    }
//
//    /**
//     * Build specification by post query.
//     *
//     * @param postQuery post query must not be null
//     * @return a post specification
//     */
//    @NonNull
//    private Specification<Post> buildSpecByQuery(@NonNull PostQuery postQuery) {
//        Assert.notNull(postQuery, "Post query must not be null");
//
//        return (Specification<Post>) (root, query, criteriaBuilder) -> {
//            List<Predicate> predicates = new LinkedList<>();
//
//            if (postQuery.getStatus() != null) {
//                predicates.add(criteriaBuilder.equal(root.get("status"), postQuery.getStatus()));
//            }
//
//            if (postQuery.getCategoryId() != null) {
//                Subquery<Post> postSubquery = query.subquery(Post.class);
//                Root<PostCategory> postCategoryRoot = postSubquery.from(PostCategory.class);
//                postSubquery.select(postCategoryRoot.get("postId"));
//                postSubquery.where(
//                        criteriaBuilder.equal(root.get("id"), postCategoryRoot.get("postId")),
//                        criteriaBuilder.equal(postCategoryRoot.get("categoryId"), postQuery.getCategoryId()));
//                predicates.add(criteriaBuilder.exists(postSubquery));
//            }
//
//            if (postQuery.getKeyword() != null) {
//                // Format like condition
//                String likeCondition = String
//                        .format("%%%s%%", StringUtils.strip(postQuery.getKeyword()));
//
//                // Build like predicate
//                Predicate titleLike = criteriaBuilder.like(root.get("title"), likeCondition);
//                Predicate originalContentLike = criteriaBuilder
//                        .like(root.get("originalContent"), likeCondition);
//
//                predicates.add(criteriaBuilder.or(titleLike, originalContentLike));
//            }
//
//            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
//        };
//    }
//
//    private PostDetailVO createOrUpdate(@NonNull Post post, Set<Integer> tagIds,
//            Set<Integer> categoryIds, Set<PostMeta> metas) {
//        Assert.notNull(post, "Post param must not be null");
//
//        // Create or update post
//        post = super.createOrUpdateBy(post);
//
//        postTagService.removeByPostId(post.getId());
//
//        postCategoryService.removeByPostId(post.getId());
//
//        // List all tags
//        List<Tag> tags = tagService.listAllByIds(tagIds);
//
//        // List all categories
//        List<Category> categories = categoryService.listAllByIds(categoryIds);
//
//        // Create post tags
//        List<PostTag> postTags = postTagService.mergeOrCreateByIfAbsent(post.getId(),
//                ServiceUtils.fetchProperty(tags, Tag::getId));
//
//        log.debug("Created post tags: [{}]", postTags);
//
//        // Create post categories
//        List<PostCategory> postCategories = postCategoryService.mergeOrCreateByIfAbsent(post.getId(),
//                ServiceUtils.fetchProperty(categories, Category::getId));
//
//        log.debug("Created post categories: [{}]", postCategories);
//
//        // Create post meta data
//        List<PostMeta> postMetaList = postMetaService
//                .createOrUpdateByPostId(post.getId(), metas);
//        log.debug("Created post metas: [{}]", postMetaList);
//
//        // Convert to post detail vo
//        return convertTo(post, tags, categories, postMetaList);
//    }
//
//    @Override
//    public void publishVisitEvent(Integer postId) {
//        eventPublisher.publishEvent(new PostVisitEvent(this, postId));
//    }
//
//    @Override
//    public @NotNull Sort getPostDefaultSort() {
//        String indexSort = optionService.getByPropertyOfNonNull(PostProperties.INDEX_SORT)
//                .toString();
//        return Sort.by(DESC, "topPriority").and(Sort.by(DESC, indexSort).and(Sort.by(DESC, "id")));
//    }
//
//    private String buildFullPath(Post post) {
//
//        PostPermalinkType permalinkType = optionService.getPostPermalinkType();
//
//        String pathSuffix = optionService.getPathSuffix();
//
//        String archivesPrefix = optionService.getArchivesPrefix();
//
//        int month = DateUtil.month(post.getCreateTime()) + 1;
//
//        String monthString = month < 10 ? "0" + month : String.valueOf(month);
//
//        int day = DateUtil.dayOfMonth(post.getCreateTime());
//
//        String dayString = day < 10 ? "0" + day : String.valueOf(day);
//
//        StringBuilder fullPath = new StringBuilder();
//
//        if (optionService.isEnabledAbsolutePath()) {
//            fullPath.append(optionService.getBlogBaseUrl());
//        }
//
//        fullPath.append(URL_SEPARATOR);
//
//        if (permalinkType.equals(PostPermalinkType.DEFAULT)) {
//            fullPath.append(archivesPrefix)
//                    .append(URL_SEPARATOR)
//                    .append(post.getSlug())
//                    .append(pathSuffix);
//        } else if (permalinkType.equals(PostPermalinkType.ID)) {
//            fullPath.append("?p=")
//                    .append(post.getId());
//        } else if (permalinkType.equals(PostPermalinkType.DATE)) {
//            fullPath.append(DateUtil.year(post.getCreateTime()))
//                    .append(URL_SEPARATOR)
//                    .append(monthString)
//                    .append(URL_SEPARATOR)
//                    .append(post.getSlug())
//                    .append(pathSuffix);
//        } else if (permalinkType.equals(PostPermalinkType.DAY)) {
//            fullPath.append(DateUtil.year(post.getCreateTime()))
//                    .append(URL_SEPARATOR)
//                    .append(monthString)
//                    .append(URL_SEPARATOR)
//                    .append(dayString)
//                    .append(URL_SEPARATOR)
//                    .append(post.getSlug())
//                    .append(pathSuffix);
//        } else if (permalinkType.equals(PostPermalinkType.YEAR)) {
//            fullPath.append(DateUtil.year(post.getCreateTime()))
//                    .append(URL_SEPARATOR)
//                    .append(post.getSlug())
//                    .append(pathSuffix);
//        }
//        return fullPath.toString();
//    }
}
