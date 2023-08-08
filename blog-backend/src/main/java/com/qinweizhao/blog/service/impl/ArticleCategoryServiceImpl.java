package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.mapper.CategoryMapper;
import com.qinweizhao.blog.mapper.ArticleCategoryMapper;
import com.qinweizhao.blog.mapper.ArticleMapper;
import com.qinweizhao.blog.model.convert.ArticleConvert;
import com.qinweizhao.blog.model.convert.CategoryConvert;
import com.qinweizhao.blog.model.dto.ArticleSimpleDTO;
import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.entity.Article;
import com.qinweizhao.blog.model.entity.Category;
import com.qinweizhao.blog.model.entity.ArticleCategory;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import com.qinweizhao.blog.service.SettingService;
import com.qinweizhao.blog.service.ArticleCategoryService;
import com.qinweizhao.blog.util.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Post category service implementation.
 *
 * @author qinweizhao
 * @since 2019-03-19
 */
@Service
@AllArgsConstructor
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory> implements ArticleCategoryService {


    private final ArticleMapper articleMapper;

    private final CategoryMapper categoryMapper;

    private final ArticleCategoryMapper articleCategoryMapper;

    private final SettingService settingService;


    @Override
    public Map<Integer, List<CategoryDTO>> listCategoryListMap(Collection<Integer> articleIds) {
        if (CollectionUtils.isEmpty(articleIds)) {
            return Collections.emptyMap();
        }

        // 查询所有关联关系
        List<ArticleCategory> postCategories = this.baseMapper.selectListByarticleIds(articleIds);

        // 获取分类 id
        Set<Integer> categoryIds = ServiceUtils.fetchProperty(postCategories, ArticleCategory::getCategoryId);

        if (CollectionUtils.isEmpty(categoryIds)) {
            return Collections.emptyMap();
        }

        // 查询所有分类
        List<CategoryDTO> categories = CategoryConvert.INSTANCE.convertToDTO(categoryMapper.selectBatchIds(categoryIds));

        // 转换为 map
        Map<Integer, CategoryDTO> categoryMap = ServiceUtils.convertToMap(categories, CategoryDTO::getId);

        // 创建新的结构
        Map<Integer, List<CategoryDTO>> categoryListMap = new LinkedHashMap<>();

        // 查找并收集
        postCategories.forEach(articleCategory -> categoryListMap.computeIfAbsent(articleCategory.getArticleId(), articleId -> new LinkedList<>())
                .add(categoryMap.get(articleCategory.getCategoryId())));

        return categoryListMap;
    }

    @Override
    public List<CategoryDTO> listByarticleId(Integer articleId) {
        Set<Integer> categoryIds = articleCategoryMapper.selectSetCategoryIdsByarticleId(articleId);
        List<Category> categories = categoryMapper.selectListByIds(categoryIds);
        List<CategoryDTO> result = CategoryConvert.INSTANCE.convertToDTO(categories);
        result.forEach(item -> item.setFullPath(settingService.buildFullPath(articleId)));
        return result;
    }

    @Override
    public List<ArticleSimpleDTO> listPostByCategoryIdAndPostStatus(Integer categoryId, ArticleStatus status) {
        Set<Integer> articleIds = articleCategoryMapper.selectsetArticleIdByCategoryIdAndPostStatus(categoryId, status);
        List<Article> articles = articleMapper.selectListByIds(articleIds);
        List<ArticleSimpleDTO> result = ArticleConvert.INSTANCE.convertToSimpleDTO(articles);
        result.forEach(item -> item.setFullPath(settingService.buildFullPath(item.getId())));
        return result;
    }

    @Override
    public List<ArticleSimpleDTO> listPostByCategorySlugAndPostStatus(String categorySlug, ArticleStatus status) {
        Category category = categoryMapper.selectBySlug(categorySlug);
        Set<Integer> articleIds = articleCategoryMapper.selectsetArticleIdByCategoryIdAndPostStatus(category.getId(), status);
        List<Article> articles = articleMapper.selectListByIds(articleIds);
        return ArticleConvert.INSTANCE.convertToSimpleDTO(articles);
    }
}
