package com.qinweizhao.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qinweizhao.blog.exception.AlreadyExistsException;
import com.qinweizhao.blog.exception.ServiceException;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.framework.event.post.PostVisitEvent;
import com.qinweizhao.blog.mapper.ContentMapper;
import com.qinweizhao.blog.mapper.ArticleCategoryMapper;
import com.qinweizhao.blog.mapper.ArticleMapper;
import com.qinweizhao.blog.mapper.ArticleTagMapper;
import com.qinweizhao.blog.model.convert.ArticleConvert;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.*;
import com.qinweizhao.blog.model.entity.Article;
import com.qinweizhao.blog.model.entity.Content;
import com.qinweizhao.blog.model.entity.ArticleCategory;
import com.qinweizhao.blog.model.entity.ArticleTag;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import com.qinweizhao.blog.model.param.ArticleParam;
import com.qinweizhao.blog.model.param.ArticleQueryParam;
import com.qinweizhao.blog.model.vo.ArchiveMonthVO;
import com.qinweizhao.blog.model.vo.ArchiveYearVO;
import com.qinweizhao.blog.service.SettingService;
import com.qinweizhao.blog.service.ArticleCategoryService;
import com.qinweizhao.blog.service.ArticleService;
import com.qinweizhao.blog.service.ArticleTagService;
import com.qinweizhao.blog.util.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
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


/**
 * 文章管理
 *
 * @author qinweizhao
 * @since 2019-03-14
 */
@Slf4j
@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private static final String CHINESE_REGEX = "[^\\x00-\\xff]";
    private static final String PUNCTUATION_REGEX = "[\\p{P}\\p{S}\\p{Z}\\s]+";
    private final ContentMapper contentMapper;
    private final Pattern summaryPattern = Pattern.compile("[\t\r\n]");
    private final ArticleMapper articleMapper;

    private final SettingService settingService;

    private final ArticleTagMapper articleTagMapper;

    private final ArticleTagService articleTagService;

    private final ArticleCategoryMapper articleCategoryMapper;

    private final ArticleCategoryService articleCategoryService;

    private final AbstractStringCacheStore cacheStore;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public PageResult<ArticleListDTO> page(ArticleQueryParam param) {


        PageResult<Article> pageResult = articleMapper.selectPage(param);
        List<Article> pageContent = pageResult.getContent();
        List<ArticleSimpleDTO> posts = ArticleConvert.INSTANCE.convertToSimpleDTO(pageContent);

        Set<Integer> postIds = ServiceUtils.fetchProperty(posts, ArticleSimpleDTO::getId);


        Map<Integer, List<TagDTO>> tagListMap = articleTagService.listTagListMapBy(postIds);
        Map<Integer, List<CategoryDTO>> categoryListMap = articleCategoryService.listCategoryListMap(postIds);

        List<ArticleListDTO> collect = posts.stream().map(post -> {
            ArticleListDTO articleListDTO = ArticleConvert.INSTANCE.convertToListDTO(post);
            articleListDTO.setTags(new ArrayList<>(Optional.ofNullable(tagListMap.get(post.getId())).orElseGet(LinkedList::new)));
            articleListDTO.setCategories(new ArrayList<>(Optional.ofNullable(categoryListMap.get(post.getId())).orElseGet(LinkedList::new)));
            articleListDTO.setFullPath(settingService.buildFullPath(post.getId()));
            return articleListDTO;
        }).collect(Collectors.toList());

        return new PageResult<>(collect, pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal(), pageResult.hasPrevious(), pageResult.hasNext());
    }

    @Override
    public PageResult<ArticleSimpleDTO> pageSimple(ArticleQueryParam param) {
        PageResult<Article> pageResult = articleMapper.selectPageSimple(param);
        List<Article> pageContent = pageResult.getContent();
        List<ArticleSimpleDTO> posts = ArticleConvert.INSTANCE.convertToSimpleDTO(pageContent);

        Set<Integer> postIds = ServiceUtils.fetchProperty(posts, ArticleSimpleDTO::getId);

        Map<Integer, List<CategoryDTO>> categoryListMap = articleCategoryService.listCategoryListMap(postIds);

        posts.forEach(post -> {
            post.setCategories(new ArrayList<>(Optional.ofNullable(categoryListMap.get(post.getId())).orElseGet(LinkedList::new)));
            post.setFullPath(settingService.buildFullPath(post.getId()));
        });

        return new PageResult<>(posts, pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal(), pageResult.hasPrevious(), pageResult.hasNext());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(ArticleParam param) {
        Article article = ArticleConvert.INSTANCE.convert(param);

        String slug = article.getSlug();

        if (ObjectUtils.isEmpty(slug)) {
            article.setSlug(SlugUtils.slug(article.getTitle()));
        } else {
            article.setSlug(SlugUtils.slug(slug));
        }

        this.slugMustNotExist(article);
        Set<Integer> categoryIds = param.getCategoryIds();
        Set<Integer> tagIds = param.getTagIds();

        String originalContent = param.getOriginalContent();

        String summary = article.getSummary();
        if (ObjectUtils.isEmpty(summary)) {
            article.setSummary(generateSummary(MarkdownUtils.renderHtml(originalContent)));
        }

        article.setWordCount(htmlFormatWordCount(originalContent));

        articleMapper.insert(article);
        Integer postId = article.getId();

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
            List<ArticleCategory> postCategories = categoryIds.stream().map(categoryId -> {
                ArticleCategory articleCategory = new ArticleCategory();
                articleCategory.setArticleId(postId);
                articleCategory.setCategoryId(categoryId);
                return articleCategory;
            }).collect(Collectors.toList());
            articleCategoryService.saveBatch(postCategories);
        }

        if (!CollectionUtils.isEmpty(tagIds)) {
            // 保存 文章-标签 关联
            List<ArticleTag> articleTags = tagIds.stream().map(tagId -> {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(postId);
                articleTag.setTagId(tagId);
                return articleTag;
            }).collect(Collectors.toList());
            articleTagService.saveBatch(articleTags);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Integer postId, ArticleParam param) {
        Article article = ArticleConvert.INSTANCE.convert(param);
        article.setId(postId);

        this.slugMustNotExist(article);

        String summary = article.getSummary();
        if (ObjectUtils.isEmpty(summary)) {
            article.setSummary(generateSummary(MarkdownUtils.renderHtml(param.getOriginalContent())));
        }

        article.setWordCount(htmlFormatWordCount(param.getOriginalContent()));

        articleMapper.updateById(article);

        String originalContent = param.getOriginalContent();
        Content content = new Content();
        content.setPostId(postId);
        content.setOriginalContent(originalContent);
        content.setContent(MarkdownUtils.renderHtml(originalContent));
        contentMapper.updateById(content);

        // 标签
        Set<Integer> tagIds = CollUtil.emptyIfNull(param.getTagIds());
        Set<Integer> dbTagIds = articleTagMapper.selectTagIdsByPostId(postId);
        Collection<Integer> addTagIds = CollUtil.subtract(tagIds, dbTagIds);
        Collection<Integer> removeTagIds = CollUtil.subtract(dbTagIds, tagIds);
        if (!CollectionUtils.isEmpty(removeTagIds)) {
            int i = articleTagMapper.deleteBatchByPostIdAndTagIds(postId, removeTagIds);
            log.debug("删除文章和标签关联记录{}条", i);
        }

        // 分类
        Set<Integer> categoryIds = CollUtil.emptyIfNull(param.getCategoryIds());
        Set<Integer> dbCategoryIds = articleCategoryMapper.selectSetCategoryIdsByPostId(postId);
        Collection<Integer> addCategoryIds = CollUtil.subtract(categoryIds, dbCategoryIds);
        Collection<Integer> removeCategoryIds = CollUtil.subtract(dbCategoryIds, categoryIds);
        if (!CollectionUtils.isEmpty(removeCategoryIds)) {
            int i = articleCategoryMapper.deleteBatchByPostIdAndTagIds(postId, removeCategoryIds);
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
     * @param article post
     */
    private void slugMustNotExist(Article article) {

        boolean exist;

        String slug = article.getSlug();
        Integer id = article.getId();
        Integer dbId = articleMapper.selectIdBySlug(slug);

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
            throw new AlreadyExistsException("文章别名 " + article.getSlug() + " 已存在");
        }
    }

    @Override
    public long countByStatus(ArticleStatus published) {
        return articleMapper.selectCountByStatus(published);
    }

    @Override
    public long countVisit() {
        return articleMapper.selectCountVisits();
    }

    @Override
    public String getPreviewUrl(Integer postId) {
        String token = IdUtil.simpleUUID();

        // 缓存预览文章(草稿状态)携带的 token
        cacheStore.putAny(token, token, 10, TimeUnit.MINUTES);


        return settingService.buildFullPath(postId) + "?token=" + token;
    }

    @Override
    @Transactional
    public boolean removeById(Integer postId) {
        Assert.notNull(postId, "文章编号不能为空");

        // 标签
        int i1 = articleTagMapper.deleteByPostId(postId);
        // 分类
        int i2 = articleCategoryMapper.deleteByPostId(postId);


        log.debug("删除和标签关联{}条，和分类{}条。", i1, i2);

        int i = contentMapper.deleteById(postId);

        log.debug("删除文章内容{}条", i);

        return articleMapper.deleteById(postId) > 0;
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
    public boolean updateStatus(ArticleStatus status, Integer postId) {
        Article article = articleMapper.selectById(postId);

        if (!(status.getValue().equals(article.getStatus()))) {

            int updatedRows = articleMapper.updateStatusById(status.getValue(), postId);
            if (updatedRows != 1) {
                throw new ServiceException("无法更新状态,id ： " + postId);
            }
        }

        // 同步内容
        if (ArticleStatus.PUBLISHED.equals(status)) {
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
    public boolean updateStatusByIds(List<Integer> ids, ArticleStatus status) {
        ids.forEach(item -> this.updateStatus(status, item));
        return true;
    }

    @Override
    public ArticleStatus getStatusById(Integer postId) {
        return articleMapper.selectStatusById(postId);
    }

    @Override
    public ArticleDTO getPrevPost(Integer postId) {
        Integer id = articleMapper.selectPrevIdByIdAndStatus(postId, ArticleStatus.PUBLISHED);
        if (ObjectUtils.isEmpty(id)) {
            return null;
        }
        return this.getById(id);
    }

    @Override
    public ArticleDTO getNextPost(Integer postId) {
        Integer id = articleMapper.selectNextIdByIdAndStatus(postId, ArticleStatus.PUBLISHED);
        if (ObjectUtils.isEmpty(id)) {
            return null;
        }
        return this.getById(id);
    }

    @Override
    public long count() {
        return articleMapper.selectCount(Wrappers.emptyWrapper());
    }

    @Override
    public boolean increaseVisit(Integer postId) {
        Article article = articleMapper.selectById(postId);
        article.setVisits(article.getVisits() + 1);
        return articleMapper.updateById(article) > 0;
    }

    @Override
    public void publishVisitEvent(Integer postId) {
        eventPublisher.publishEvent(new PostVisitEvent(this, postId));
    }

    @Override
    public ArticleDTO getById(Integer postId) {

        Article article = articleMapper.selectById(postId);

        ArticleDTO articleDTO = ArticleConvert.INSTANCE.convert(article);

        List<TagDTO> tags = articleTagService.listTagsByPostId(article.getId());

        List<CategoryDTO> categories = articleCategoryService.listByPostId(article.getId());

        Content content = contentMapper.selectById(postId);
        String originalContent = content.getOriginalContent();
        articleDTO.setFormatContent(content.getContent());
        articleDTO.setOriginalContent(originalContent);

        Set<Integer> tagIds = ServiceUtils.fetchProperty(tags, TagDTO::getId);


        Set<Integer> categoryIds = ServiceUtils.fetchProperty(categories, CategoryDTO::getId);

        articleDTO.setTagIds(tagIds);
        articleDTO.setTags(tags);

        articleDTO.setCategoryIds(categoryIds);
        articleDTO.setCategories(categories);

        articleDTO.setFullPath(settingService.buildFullPath(article.getId()));

        return articleDTO;
    }

    @Override
    public ArticleSimpleDTO getSimpleById(Integer postId) {
        Article article = articleMapper.selectById(postId);
        ArticleSimpleDTO result = ArticleConvert.INSTANCE.convertSimpleDTO(article);
        result.setFullPath(settingService.buildFullPath(article.getId()));
        return result;
    }

    @Override
    public String generateDescription(String content) {

        String text = HaloUtils.cleanHtmlTag(content);

        Matcher matcher = summaryPattern.matcher(text);
        text = matcher.replaceAll("");

        // 给默认值
        int summaryLength = Integer.parseInt(String.valueOf(settingService.get("post_summary_length")));

        return StringUtils.substring(text, 0, summaryLength);
    }


    @Override
    public List<ArchiveYearVO> listYearArchives() {

        List<Article> articles = articleMapper.selectListByStatus(ArticleStatus.PUBLISHED);

        Map<Integer, List<ArticleSimpleDTO>> yearPostMap = new HashMap<>(8);

        List<ArticleSimpleDTO> postSimples = ArticleConvert.INSTANCE.convertToSimpleDTO(articles);


        postSimples.forEach(post -> {
            LocalDateTime createTime = post.getCreateTime();
            post.setFullPath(settingService.buildFullPath(post.getId()));
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
        List<Article> articles = articleMapper.selectListByStatus(ArticleStatus.PUBLISHED);

        List<ArticleSimpleDTO> postSimples = ArticleConvert.INSTANCE.convertToSimpleDTO(articles);

        Map<Integer, Map<Integer, List<ArticleSimpleDTO>>> yearMonthPostMap = new HashMap<>(8);

        postSimples.forEach(post -> {
            LocalDateTime createTime = post.getCreateTime();
            post.setFullPath(settingService.buildFullPath(post.getId()));
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

            Article article = new Article();
            article.setTitle(String.valueOf(LocalDateTime.now()));
            article.setSlug(String.valueOf(LocalDateTime.now()));
            article.setSummary(generateSummary(MarkdownUtils.renderHtml(originalContent)));
            article.setWordCount(htmlFormatWordCount(originalContent));
            article.setStatus(ArticleStatus.DRAFT.getValue());

            articleMapper.insert(article);

            Integer id = article.getId();

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
    public List<ArticleSimpleDTO> listSimple(int top) {
        List<Article> articles = articleMapper.selectListLatest(top);
        List<ArticleSimpleDTO> result = ArticleConvert.INSTANCE.convertToSimpleDTO(articles);
        result.forEach(item -> item.setFullPath(settingService.buildFullPath(item.getId())));
        return result;
    }


    protected String generateSummary(String htmlContent) {
        Assert.notNull(htmlContent, "文章原始内容不能为空");

        String text = HaloUtils.cleanHtmlTag(htmlContent);

        Matcher matcher = summaryPattern.matcher(text);
        text = matcher.replaceAll("");

        int summaryLength = Integer.parseInt(String.valueOf(settingService.get("post_summary_length")));

        return StringUtils.substring(text, 0, summaryLength);
    }


}
