package com.qinweizhao.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qinweizhao.blog.exception.AlreadyExistsException;
import com.qinweizhao.blog.exception.ServiceException;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.framework.event.post.PostVisitEvent;
import com.qinweizhao.blog.mapper.*;
import com.qinweizhao.blog.model.convert.PostConvert;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.*;
import com.qinweizhao.blog.model.entity.Content;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.entity.PostCategory;
import com.qinweizhao.blog.model.entity.PostTag;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.PostParam;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.model.properties.PostProperties;
import com.qinweizhao.blog.model.vo.ArchiveMonthVO;
import com.qinweizhao.blog.model.vo.ArchiveYearVO;
import com.qinweizhao.blog.service.*;
import com.qinweizhao.blog.util.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.qinweizhao.blog.model.support.BlogConst.URL_SEPARATOR;

/**
 * 文章管理
 *
 * @author qinweizhao
 * @since 2019-03-14
 */
@Slf4j
@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final ContentMapper contentMapper;

    private final Pattern summaryPattern = Pattern.compile("[\t\r\n]");

    private static final String CHINESE_REGEX = "[^\\x00-\\xff]";

    private static final String PUNCTUATION_REGEX = "[\\p{P}\\p{S}\\p{Z}\\s]+";

    private final PostMapper postMapper;

    private final ConfigService configService;

    private final PostTagMapper postTagMapper;

    private final PostTagService postTagService;

    private final CommentMapper commentMapper;

    private final CommentService commentService;

    private final PostCategoryMapper postCategoryMapper;

    private final PostCategoryService postCategoryService;

    private final AbstractStringCacheStore cacheStore;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public PageResult<PostListDTO> page(PostQueryParam param) {


        PageResult<Post> pageResult = postMapper.selectPage(param);
        List<Post> pageContent = pageResult.getContent();
        List<PostSimpleDTO> posts = PostConvert.INSTANCE.convertToSimpleDTO(pageContent);

        Set<Integer> postIds = ServiceUtils.fetchProperty(posts, PostSimpleDTO::getId);


        Map<Integer, List<TagDTO>> tagListMap = postTagService.listTagListMapBy(postIds);
        Map<Integer, List<CategoryDTO>> categoryListMap = postCategoryService.listCategoryListMap(postIds);
        Map<Integer, Long> commentCountMap = commentService.countByTypeAndTargetIds(CommentType.POST, postIds);


        List<PostListDTO> collect = posts.stream().map(post -> {
            PostListDTO postListDTO = PostConvert.INSTANCE.convertToListDTO(post);
            postListDTO.setTags(new ArrayList<>(Optional.ofNullable(tagListMap.get(post.getId())).orElseGet(LinkedList::new)));
            postListDTO.setCategories(new ArrayList<>(Optional.ofNullable(categoryListMap.get(post.getId())).orElseGet(LinkedList::new)));
            postListDTO.setCommentCount(commentCountMap.getOrDefault(post.getId(), 0L));
            postListDTO.setFullPath(buildFullPath(post.getId()));
            return postListDTO;
        }).collect(Collectors.toList());

        return new PageResult<>(collect, pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal(), pageResult.hasPrevious(), pageResult.hasNext());
    }

    @Override
    public PageResult<PostSimpleDTO> pageSimple(PostQueryParam param) {
        PageResult<Post> pageResult = postMapper.selectPageSimple(param);
        List<Post> pageContent = pageResult.getContent();
        List<PostSimpleDTO> posts = PostConvert.INSTANCE.convertToSimpleDTO(pageContent);

        Set<Integer> postIds = ServiceUtils.fetchProperty(posts, PostSimpleDTO::getId);

        Map<Integer, Long> commentCountMap = commentService.countByTypeAndTargetIds(CommentType.POST, postIds);

        Map<Integer, List<CategoryDTO>> categoryListMap = postCategoryService.listCategoryListMap(postIds);

        posts.forEach(post -> {
            post.setCategories(new ArrayList<>(Optional.ofNullable(categoryListMap.get(post.getId())).orElseGet(LinkedList::new)));
            post.setCommentCount(commentCountMap.getOrDefault(post.getId(), 0L));
            post.setFullPath(buildFullPath(post.getId()));
        });

        return new PageResult<>(posts, pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal(), pageResult.hasPrevious(), pageResult.hasNext());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(PostParam param) {
        Post post = PostConvert.INSTANCE.convert(param);

        String slug = post.getSlug();

        if (ObjectUtils.isEmpty(slug)) {
            post.setSlug(SlugUtils.slug(post.getTitle()));
        } else {
            post.setSlug(SlugUtils.slug(slug));
        }

        this.slugMustNotExist(post);
        Set<Integer> categoryIds = param.getCategoryIds();
        Set<Integer> tagIds = param.getTagIds();

        String originalContent = param.getOriginalContent();

        String summary = post.getSummary();
        if (ObjectUtils.isEmpty(summary)) {
            post.setSummary(generateSummary(MarkdownUtils.renderHtml(originalContent)));
        }

        post.setWordCount(htmlFormatWordCount(originalContent));

        postMapper.insert(post);
        Integer postId = post.getId();

        Content content = new Content();
        content.setOriginalContent(originalContent);
        content.setContent(MarkdownUtils.renderHtml(originalContent));
        content.setPostId(postId);
        contentMapper.insert(content);

        this.savePostRelation(categoryIds, tagIds, postId);

        return postId;
    }

    /**
     * 保存文章和分类/标签 关联关系
     *
     * @param categoryIds categoryIds
     * @param tagIds      tagIds
     * @param postId      postId
     */
    private void savePostRelation(Collection<Integer> categoryIds, Collection<Integer> tagIds, Integer postId) {
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

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Integer postId, PostParam param) {
        Post post = PostConvert.INSTANCE.convert(param);
        post.setId(postId);

        this.slugMustNotExist(post);

        String summary = post.getSummary();
        if (ObjectUtils.isEmpty(summary)) {
            post.setSummary(generateSummary(MarkdownUtils.renderHtml(param.getOriginalContent())));
        }

        post.setWordCount(htmlFormatWordCount(param.getOriginalContent()));

        postMapper.updateById(post);

        String originalContent = param.getOriginalContent();
        Content content = new Content();
        content.setPostId(postId);
        content.setOriginalContent(originalContent);
        content.setContent(MarkdownUtils.renderHtml(originalContent));
        contentMapper.updateById(content);

        // 标签
        Set<Integer> tagIds = CollUtil.emptyIfNull(param.getTagIds());
        Set<Integer> dbTagIds = postTagMapper.selectTagIdsByPostId(postId);
        Collection<Integer> addTagIds = CollUtil.subtract(tagIds, dbTagIds);
        Collection<Integer> removeTagIds = CollUtil.subtract(dbTagIds, tagIds);
        if (!CollectionUtils.isEmpty(removeTagIds)) {
            int i = postTagMapper.deleteBatchByPostIdAndTagIds(postId, removeTagIds);
            log.debug("删除文章和标签关联记录{}条", i);
        }

        // 分类
        Set<Integer> categoryIds = CollUtil.emptyIfNull(param.getCategoryIds());
        Set<Integer> dbCategoryIds = postCategoryMapper.selectSetCategoryIdsByPostId(postId);
        Collection<Integer> addCategoryIds = CollUtil.subtract(categoryIds, dbCategoryIds);
        Collection<Integer> removeCategoryIds = CollUtil.subtract(dbCategoryIds, categoryIds);
        if (!CollectionUtils.isEmpty(removeCategoryIds)) {
            int i = postCategoryMapper.deleteBatchByPostIdAndTagIds(postId, removeCategoryIds);
            log.debug("删除文章和分类关联记录{}条", i);
        }

        this.savePostRelation(addCategoryIds, addTagIds, postId);
        return true;
    }


    public long htmlFormatWordCount(String htmlContent) {
        if (htmlContent == null) {
            return 0;
        }

        String cleanContent = HaloUtils.cleanHtmlTag(htmlContent);

        String tempString = cleanContent.replaceAll(CHINESE_REGEX, "");

        String otherString = cleanContent.replaceAll(CHINESE_REGEX, " ");

        int chineseWordCount = cleanContent.length() - tempString.length();

        String[] otherWords = otherString.split(PUNCTUATION_REGEX);

        long otherWordLength = otherWords.length;

        if (otherWordLength > 0 && otherWords[0].length() == 0) {
            otherWordLength--;
        }

        if (otherWords.length > 1 && otherWords[otherWords.length - 1].length() == 0) {
            otherWordLength--;
        }

        return chineseWordCount + otherWordLength;
    }


    /**
     * 文章别名必须唯一
     *
     * @param post post
     */
    private void slugMustNotExist(Post post) {

        boolean exist;

        String slug = post.getSlug();
        Integer id = post.getId();
        Integer dbId = postMapper.selectIdBySlug(slug);

        // 如果为 null 则一定不存在
        if (dbId == null) {
            return;
        }
        // 新增
        if (ObjectUtils.isEmpty(id)) {
            exist = true;
        } else {
            // 更新
            exist = !Objects.equals(dbId, id);
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
        String token = IdUtil.simpleUUID();

        // 缓存预览文章(草稿状态)携带的 token
        cacheStore.putAny(token, token, 10, TimeUnit.MINUTES);


        return this.buildFullPath(postId) + "&token=" + token;
    }

    @Override
    @Transactional
    public boolean removeById(Integer postId) {
        Assert.notNull(postId, "文章编号不能为空");

        // 标签
        int i1 = postTagMapper.deleteByPostId(postId);
        // 分类
        int i2 = postCategoryMapper.deleteByPostId(postId);

        // 评论
        int i3 = commentMapper.deleteByPostId(postId);

        log.debug("删除和标签关联{}条，和分类{}条，评论{}条。", i1, i2, i3);

        int i = contentMapper.deleteById(postId);

        log.debug("删除文章内容{}条", i);

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
    public PostStatus getStatusById(Integer postId) {
        return postMapper.selectStatusById(postId);
    }

    @Override
    public PostDTO getPrevPost(Integer postId) {
        Integer id = postMapper.selectPrevIdByIdAndStatus(postId, PostStatus.PUBLISHED);
        if (ObjectUtils.isEmpty(id)) {
            return null;
        }
        return this.getById(id);
    }

    @Override
    public PostDTO getNextPost(Integer postId) {
        Integer id = postMapper.selectNextIdByIdAndStatus(postId, PostStatus.PUBLISHED);
        if (ObjectUtils.isEmpty(id)) {
            return null;
        }
        return this.getById(id);
    }

    @Override
    public long count() {
        return postMapper.selectCount(Wrappers.emptyWrapper());
    }

    @Override
    public boolean increaseVisit(Integer postId) {
        Post post = postMapper.selectById(postId);
        post.setVisits(post.getVisits() + 1);
        return postMapper.updateById(post) > 0;
    }

    @Override
    public boolean increaseLike(Integer postId) {
        Post post = postMapper.selectById(postId);
        post.setLikes(post.getLikes() + 1);
        return postMapper.updateById(post) > 0;
    }

    @Override
    public void publishVisitEvent(Integer postId) {
        eventPublisher.publishEvent(new PostVisitEvent(this, postId));
    }

    @Override
    public PostDTO getById(Integer postId) {

        Post post = postMapper.selectById(postId);

        PostDTO postDTO = PostConvert.INSTANCE.convert(post);

        List<TagDTO> tags = postTagService.listTagsByPostId(post.getId());

        List<CategoryDTO> categories = postCategoryService.listByPostId(post.getId());

        Content content = contentMapper.selectById(postId);
        String originalContent = content.getOriginalContent();
        postDTO.setFormatContent(content.getContent());
        postDTO.setOriginalContent(originalContent);

        Set<Integer> tagIds = ServiceUtils.fetchProperty(tags, TagDTO::getId);


        Set<Integer> categoryIds = ServiceUtils.fetchProperty(categories, CategoryDTO::getId);

        postDTO.setTagIds(tagIds);
        postDTO.setTags(tags);

        postDTO.setCategoryIds(categoryIds);
        postDTO.setCategories(categories);

        postDTO.setFullPath(buildFullPath(post.getId()));

        postDTO.setCommentCount(commentMapper.selectCountByPostIdAndStatus(postId, CommentStatus.PUBLISHED));

        return postDTO;
    }

    @Override
    public PostSimpleDTO getSimpleById(Integer postId) {
        Post post = postMapper.selectById(postId);
        PostSimpleDTO result = PostConvert.INSTANCE.convertSimpleDTO(post);
        result.setFullPath(buildFullPath(post.getId()));
        return result;
    }

    @Override
    public String generateDescription(String content) {

        String text = HaloUtils.cleanHtmlTag(content);

        Matcher matcher = summaryPattern.matcher(text);
        text = matcher.replaceAll("");

        Integer summaryLength = configService.getByPropertyOrDefault(PostProperties.SUMMARY_LENGTH, Integer.class, 150);

        return StringUtils.substring(text, 0, summaryLength);
    }


    @Override
    public List<ArchiveYearVO> listYearArchives() {

        List<Post> posts = postMapper.selectListByStatus(PostStatus.PUBLISHED);

        Map<Integer, List<PostSimpleDTO>> yearPostMap = new HashMap<>(8);

        List<PostSimpleDTO> postSimples = PostConvert.INSTANCE.convertToSimpleDTO(posts);


        postSimples.forEach(post -> {
            LocalDateTime createTime = post.getCreateTime();
            post.setFullPath(buildFullPath(post.getId()));
            yearPostMap.computeIfAbsent(createTime.getYear(), year -> new LinkedList<>()).add(post);
        });

        List<ArchiveYearVO> archives = new LinkedList<>();

        yearPostMap.forEach((year, postList) -> {

            ArchiveYearVO archive = new ArchiveYearVO();
            archive.setYear(year);
            archive.setPosts(postList);

            archives.add(archive);
        });

        archives.sort(new ArchiveYearVO.ArchiveComparator());

        return archives;
    }

    @Override
    public List<ArchiveMonthVO> listMonthArchives() {
        List<Post> posts = postMapper.selectListByStatus(PostStatus.PUBLISHED);

        List<PostSimpleDTO> postSimples = PostConvert.INSTANCE.convertToSimpleDTO(posts);

        Map<Integer, Map<Integer, List<PostSimpleDTO>>> yearMonthPostMap = new HashMap<>(8);

        postSimples.forEach(post -> {
            LocalDateTime createTime = post.getCreateTime();
            post.setFullPath(buildFullPath(post.getId()));
            yearMonthPostMap.computeIfAbsent(createTime.getYear(), year -> new LinkedHashMap<>()).computeIfAbsent(createTime.getMonthValue(), month -> new LinkedList<>()).add(post);
        });

        List<ArchiveMonthVO> archives = new LinkedList<>();

        yearMonthPostMap.forEach((year, monthPostMap) -> monthPostMap.forEach((month, postList) -> {
            ArchiveMonthVO archive = new ArchiveMonthVO();
            archive.setYear(year);
            archive.setMonth(month);
            archive.setPosts(postList);

            archives.add(archive);
        }));

        archives.sort(new ArchiveMonthVO.ArchiveComparator());

        return archives;
    }

    @Override
    @Transactional
    public boolean importMarkdown(MultipartFile file) {
        try {
            String markdown = FileUtils.readString(file.getInputStream());
            // 内容为空
            if (ObjectUtils.isEmpty(markdown)) {
                return false;
            }

            // 原始内容
            String originalContent = HaloUtils.cleanHtmlTag(markdown);

            Post post = new Post();
            post.setTitle(String.valueOf(LocalDateTime.now()));
            post.setSlug(String.valueOf(LocalDateTime.now()));
            post.setSummary(generateSummary(MarkdownUtils.renderHtml(originalContent)));
            post.setWordCount(htmlFormatWordCount(originalContent));
            post.setStatus(PostStatus.DRAFT.getValue());

            postMapper.insert(post);

            Integer id = post.getId();

            Content content = new Content();
            content.setPostId(id);
            content.setContent(MarkdownUtils.renderHtml(originalContent));
            content.setOriginalContent(originalContent);

            contentMapper.insert(content);
            // 保存文章
        } catch (OutOfMemoryError error) {
            throw new ServiceException("文件内容过大，无法导入。", error);
        } catch (IOException e) {
            throw new ServiceException("导入失败");
        }
        return true;
    }


    @Override
    public List<PostSimpleDTO> listSimple(int top) {
        List<Post> posts = postMapper.selectListLatest(top);
        List<PostSimpleDTO> result = PostConvert.INSTANCE.convertToSimpleDTO(posts);
        result.forEach(item -> item.setFullPath(configService.buildFullPath(item.getId())));
        return result;
    }


    protected String generateSummary(@NonNull String htmlContent) {
        Assert.notNull(htmlContent, "文章原始内容不能为空");

        String text = HaloUtils.cleanHtmlTag(htmlContent);

        Matcher matcher = summaryPattern.matcher(text);
        text = matcher.replaceAll("");

        Integer summaryLength = configService.getByPropertyOrDefault(PostProperties.SUMMARY_LENGTH, Integer.class, 150);

        return StringUtils.substring(text, 0, summaryLength);
    }


    @Override
    public String buildFullPath(Integer postId) {

        return configService.getBlogBaseUrl() + URL_SEPARATOR + "?p=" + postId;
    }

}
