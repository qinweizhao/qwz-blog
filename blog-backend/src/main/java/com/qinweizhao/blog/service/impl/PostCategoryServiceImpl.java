package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.mapper.CategoryMapper;
import com.qinweizhao.blog.mapper.PostCategoryMapper;
import com.qinweizhao.blog.mapper.PostMapper;
import com.qinweizhao.blog.model.convert.CategoryConvert;
import com.qinweizhao.blog.model.convert.PostConvert;
import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.dto.PostSimpleDTO;
import com.qinweizhao.blog.model.entity.Category;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.entity.PostCategory;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.PostCategoryService;
import com.qinweizhao.blog.util.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Post category service implementation.
 *
 * @author guqing
 * @author qinweizhao
 * @since 2019-03-19
 */
@Service
@AllArgsConstructor
public class PostCategoryServiceImpl extends ServiceImpl<PostCategoryMapper, PostCategory> implements PostCategoryService {


    private final PostMapper postMapper;

    private final CategoryMapper categoryMapper;

    private final PostCategoryMapper postCategoryMapper;

    private final ConfigService configService;


    @Override
    public Map<Integer, List<CategoryDTO>> listCategoryListMap(Collection<Integer> postIds) {
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyMap();
        }

        // 查询所有关联关系
        List<PostCategory> postCategories = this.baseMapper.selectListByPostIds(postIds);

        // 获取分类 id
        Set<Integer> categoryIds = ServiceUtils.fetchProperty(postCategories, PostCategory::getCategoryId);

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
        postCategories.forEach(postCategory -> categoryListMap.computeIfAbsent(postCategory.getPostId(), postId -> new LinkedList<>())
                .add(categoryMap.get(postCategory.getCategoryId())));

        return categoryListMap;
    }

    @Override
    public List<CategoryDTO> listByPostId(Integer postId) {
        Set<Integer> categoryIds = postCategoryMapper.selectSetCategoryIdsByPostId(postId);
        List<Category> categories = categoryMapper.selectListByIds(categoryIds);
        List<CategoryDTO> result = CategoryConvert.INSTANCE.convertToDTO(categories);
        result.forEach(item -> item.setFullPath(configService.buildFullPath(postId)));
        return result;
    }

    @Override
    public List<PostSimpleDTO> listPostByCategoryIdAndPostStatus(Integer categoryId, PostStatus status) {
        Set<Integer> postIds = postCategoryMapper.selectSetPostIdByCategoryIdAndPostStatus(categoryId, status);
        List<Post> posts = postMapper.selectListByIds(postIds);
        List<PostSimpleDTO> result = PostConvert.INSTANCE.convertToSimpleDTO(posts);
        result.forEach(item -> item.setFullPath(configService.buildFullPath(item.getId())));
        return result;
    }

    @Override
    public List<PostSimpleDTO> listPostByCategorySlugAndPostStatus(String categorySlug, PostStatus status) {
        Category category = categoryMapper.selectBySlug(categorySlug);
        Set<Integer> postIds = postCategoryMapper.selectSetPostIdByCategoryIdAndPostStatus(category.getId(), status);
        List<Post> posts = postMapper.selectListByIds(postIds);
        return PostConvert.INSTANCE.convertToSimpleDTO(posts);
    }
}
