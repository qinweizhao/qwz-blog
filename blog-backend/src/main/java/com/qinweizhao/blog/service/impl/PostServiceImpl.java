package com.qinweizhao.blog.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.convert.CategoryConvert;
import com.qinweizhao.blog.convert.MetaConvert;
import com.qinweizhao.blog.convert.PostConvert;
import com.qinweizhao.blog.convert.TagConvert;
import com.qinweizhao.blog.mapper.PostMapper;
import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.post.PostSimpleDTO;
import com.qinweizhao.blog.model.entity.Category;
import com.qinweizhao.blog.model.entity.Meta;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.entity.Tag;
import com.qinweizhao.blog.model.enums.PostPermalinkType;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.model.vo.PostListVO;
import com.qinweizhao.blog.service.*;
import com.qinweizhao.blog.util.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
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
 * @date 2019-03-14
 */
@Slf4j
@Service
@AllArgsConstructor
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {


    private final OptionService optionService;

    private final PostTagService postTagService;

    private final PostCategoryService postCategoryService;

    private final CommentService commentService;

    private final MetaService metaService;


    @Override
    public long countByStatus(PostStatus published) {
        return this.baseMapper.selectCountByStatus(published);
    }

    @Override
    public long countVisit() {
        return this.baseMapper.selectCountVisits();
    }

    @Override
    public long countLike() {
        return this.baseMapper.selectCountLikes();
    }

    @Override
    public PageResult<PostSimpleDTO> pagePosts(PostQueryParam postQueryParam) {
        PageResult<Post> pageResult = this.baseMapper.selectPagePosts(postQueryParam);
        return PostConvert.INSTANCE.convertToSimpleDTO(pageResult);
    }

    @Override
    public PageResult<PostListVO> buildPostListVO(PageResult<PostSimpleDTO> postPage) {
        List<PostSimpleDTO> posts = postPage.getContent();

        Set<Integer> postIds = ServiceUtils.fetchProperty(posts, PostSimpleDTO::getId);

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


    private String buildFullPath(PostSimpleDTO post) {

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

//    private final TagService tagService;
//
//    private final CategoryService categoryService;
//
//    private final PostTagService postTagService;
//
//    private final PostCategoryService postCategoryService;
//
//    private final CommentService<BaseMapper<Comment>, BaseEntity> commentService;
//
//    private final ApplicationEventPublisher eventPublisher;
//
//    private final PostMetaService postMetaService;
//
//    private final OptionService optionService;


//    @Override
//    public Page<Post> pageBy(PostQuery postQuery, Pageable pageable) {
//        Assert.notNull(postQuery, "Post query must not be null");
//        Assert.notNull(pageable, "Page info must not be null");
//
//        // Build specification and find all
//        return postRepository.findAll(buildSpecByQuery(postQuery), pageable);
//    }
//
//    @Override
//    public Page<Post> pageBy(String keyword, Pageable pageable) {
//        Assert.notNull(keyword, "keyword must not be null");
//        Assert.notNull(pageable, "Page info must not be null");
//
//        PostQuery postQuery = new PostQuery();
//        postQuery.setKeyword(keyword);
//        postQuery.setStatus(PostStatus.PUBLISHED);
//
//        // Build specification and find all
//        return postRepository.findAll(buildSpecByQuery(postQuery), pageable);
//    }
//
//    @Override
//    @Transactional
//    public PostDetailVO createBy(Post postToCreate, Set<Integer> tagIds, Set<Integer> categoryIds,
//            Set<PostMeta> metas, boolean autoSave) {
//        PostDetailVO createdPost = createOrUpdate(postToCreate, tagIds, categoryIds, metas);
//        if (!autoSave) {
//            // Log the creation
//            LogEvent logEvent = new LogEvent(this, createdPost.getId().toString(),
//                    LogType.POST_PUBLISHED, createdPost.getTitle());
//            eventPublisher.publishEvent(logEvent);
//        }
//        return createdPost;
//    }
//
//    @Override
//    public PostDetailVO createBy(Post postToCreate, Set<Integer> tagIds, Set<Integer> categoryIds,
//            boolean autoSave) {
//        PostDetailVO createdPost = createOrUpdate(postToCreate, tagIds, categoryIds, null);
//        if (!autoSave) {
//            // Log the creation
//            LogEvent logEvent = new LogEvent(this, createdPost.getId().toString(),
//                    LogType.POST_PUBLISHED, createdPost.getTitle());
//            eventPublisher.publishEvent(logEvent);
//        }
//        return createdPost;
//    }
//
//    @Override
//    @Transactional
//    public PostDetailVO updateBy(Post postToUpdate, Set<Integer> tagIds, Set<Integer> categoryIds,
//            Set<PostMeta> metas, boolean autoSave) {
//        // Set edit time
//        postToUpdate.setEditTime(DateUtils.now());
//        PostDetailVO updatedPost = createOrUpdate(postToUpdate, tagIds, categoryIds, metas);
//        if (!autoSave) {
//            // Log the creation
//            LogEvent logEvent = new LogEvent(this, updatedPost.getId().toString(),
//                    LogType.POST_EDITED, updatedPost.getTitle());
//            eventPublisher.publishEvent(logEvent);
//        }
//        return updatedPost;
//    }
//
//    @Override
//    public Post getBy(PostStatus status, String slug) {
//        return super.getBy(status, slug);
//    }
//
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
//        return this.baseMapper.selectBySlug(slug);
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
//    public Page<PostListVO> convertToListVo(Page<Post> postPage) {
//        Assert.notNull(postPage, "Post page must not be null");
//
//        List<Post> posts = postPage.getContent();
//
//        Set<Integer> postIds = ServiceUtils.fetchProperty(posts, Post::getId);
//
//        // Get tag list map
//        Map<Integer, List<Tag>> tagListMap = postTagService.listTagListMapBy(postIds);
//
//        // Get category list map
//        Map<Integer, List<Category>> categoryListMap = postCategoryService
//                .listCategoryListMap(postIds);
//
//        // Get comment count
//        Map<Integer, Long> commentCountMap = commentService.countByPostIds(postIds);
//
//        // Get post meta list map
//        Map<Integer, List<PostMeta>> postMetaListMap = postMetaService.listPostMetaAsMap(postIds);
//
//        return postPage.map(post -> {
//            PostListVO postListVO = new PostListVO().convertFrom(post);
//
//            if (StringUtils.isBlank(postListVO.getSummary())) {
//                postListVO.setSummary(generateSummary(post.getFormatContent()));
//            }
//
//            Optional.ofNullable(tagListMap.get(post.getId())).orElseGet(LinkedList::new);
//
//            // Set tags
//            postListVO.setTags(Optional.ofNullable(tagListMap.get(post.getId()))
//                    .orElseGet(LinkedList::new)
//                    .stream()
//                    .filter(Objects::nonNull)
//                    .map(tagService::convertTo)
//                    .collect(Collectors.toList()));
//
//            // Set categories
//            postListVO.setCategories(Optional.ofNullable(categoryListMap.get(post.getId()))
//                    .orElseGet(LinkedList::new)
//                    .stream()
//                    .filter(Objects::nonNull)
//                    .map(categoryService::convertTo)
//                    .collect(Collectors.toList()));
//
//            // Set post metas
//            List<PostMeta> metas = Optional.ofNullable(postMetaListMap.get(post.getId()))
//                    .orElseGet(LinkedList::new);
//            postListVO.setMetas(postMetaService.convertToMap(metas));
//
//            // Set comment count
//            postListVO.setCommentCount(commentCountMap.getOrDefault(post.getId(), 0L));
//
//            postListVO.setFullPath(buildFullPath(post));
//
//            return postListVO;
//        });
//    }
//
//    @Override
//    public List<PostListVO> convertToListVo(List<Post> posts) {
//        Assert.notNull(posts, "Post page must not be null");
//
//        Set<Integer> postIds = ServiceUtils.fetchProperty(posts, Post::getId);
//
//        // Get tag list map
//        Map<Integer, List<Tag>> tagListMap = postTagService.listTagListMapBy(postIds);
//
//        // Get category list map
//        Map<Integer, List<Category>> categoryListMap = postCategoryService
//                .listCategoryListMap(postIds);
//
//        // Get comment count
//        Map<Integer, Long> commentCountMap = commentService.countByPostIds(postIds);
//
//        // Get post meta list map
//        Map<Integer, List<PostMeta>> postMetaListMap = postMetaService.listPostMetaAsMap(postIds);
//
//        return posts.stream().map(post -> {
//            PostListVO postListVO = new PostListVO().convertFrom(post);
//
//            if (StringUtils.isBlank(postListVO.getSummary())) {
//                postListVO.setSummary(generateSummary(post.getFormatContent()));
//            }
//
//            Optional.ofNullable(tagListMap.get(post.getId())).orElseGet(LinkedList::new);
//
//            // Set tags
//            postListVO.setTags(Optional.ofNullable(tagListMap.get(post.getId()))
//                    .orElseGet(LinkedList::new)
//                    .stream()
//                    .filter(Objects::nonNull)
//                    .map(tagService::convertTo)
//                    .collect(Collectors.toList()));
//
//            // Set categories
//            postListVO.setCategories(Optional.ofNullable(categoryListMap.get(post.getId()))
//                    .orElseGet(LinkedList::new)
//                    .stream()
//                    .filter(Objects::nonNull)
//                    .map(categoryService::convertTo)
//                    .collect(Collectors.toList()));
//
//            // Set post metas
//            List<PostMeta> metas = Optional.ofNullable(postMetaListMap.get(post.getId()))
//                    .orElseGet(LinkedList::new);
//            postListVO.setMetas(postMetaService.convertToMap(metas));
//
//            // Set comment count
//            postListVO.setCommentCount(commentCountMap.getOrDefault(post.getId(), 0L));
//
//            postListVO.setFullPath(buildFullPath(post));
//
//            return postListVO;
//        }).collect(Collectors.toList());
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
