package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.convert.CategoryConvert;
import com.qinweizhao.blog.mapper.CategoryMapper;
import com.qinweizhao.blog.mapper.PostCategoryMapper;
import com.qinweizhao.blog.mapper.PostMapper;
import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.dto.CategoryWithPostCountDTO;
import com.qinweizhao.blog.model.entity.Category;
import com.qinweizhao.blog.model.entity.PostCategory;
import com.qinweizhao.blog.model.projection.CategoryPostCountProjection;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.PostCategoryService;
import com.qinweizhao.blog.util.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.qinweizhao.blog.model.support.HaloConst.URL_SEPARATOR;

/**
 * Post category service implementation.
 *
 * @author johnniang
 * @author ryanwang
 * @author guqing
 * @author qinweizhao
 * @date 2019-03-19
 */
@Service
@AllArgsConstructor
public class PostCategoryServiceImpl extends ServiceImpl<PostCategoryMapper, PostCategory> implements PostCategoryService {


    private final PostMapper postMapper;

    private final CategoryMapper categoryMapper;

    private final PostCategoryMapper postCategoryMapper;

    private final OptionService optionService;


    @Override
    public Map<Integer, List<CategoryDTO>> listCategoryListMap(Collection<Integer> postIds) {
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyMap();
        }

        // 查询所有关联关系
        List<PostCategory> postCategories = this.baseMapper.selectListByPostIds(postIds);

        // 获取分类 id
        Set<Integer> categoryIds = ServiceUtils.fetchProperty(postCategories, PostCategory::getCategoryId);

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
    public List<CategoryDTO> listCategoriesByPostId(Integer postId) {
        Set<Integer> categoryIds = postCategoryMapper.selectSetCategoryIdsByPostId(postId);
        List<Category> categories = categoryMapper.selectListByIds(categoryIds);
        return CategoryConvert.INSTANCE.convertToDTO(categories);
    }

//    @Override
//    public List<PostSimpleDTO> listPostByCategoryIdAndPostStatus(Integer categoryId, PostStatus status) {
//        Set<Integer> postIds = postCategoryMapper.selectSetPostIdByCategoryIdAndPostStatus(categoryId, status);
//        List<Post> posts = postMapper.selectListByIds(postIds);
//        return PostConvert.INSTANCE.convertToSimpleDTO(posts);
//    }
//
//    @Override
//    public List<PostSimpleDTO> listPostByCategorySlugAndPostStatus(String categorySlug, PostStatus status) {
//        Category category = categoryMapper.selectBySlug(categorySlug);
//        Set<Integer> postIds = postCategoryMapper.selectSetPostIdByCategoryIdAndPostStatus(category.getId(), status);
//        List<Post> posts = postMapper.selectListByIds(postIds);
//        return PostConvert.INSTANCE.convertToSimpleDTO(posts);
//    }

    @Override
    public List<CategoryWithPostCountDTO> listCategoryWithPostCountDto() {

        List<Category> categories = categoryMapper.selectList();

        // 查询分类发帖数
        Map<Integer, Long> categoryPostCountMap = ServiceUtils.convertToMap(postCategoryMapper.selectPostCount(), CategoryPostCountProjection::getCategoryId, CategoryPostCountProjection::getPostCount);

        // 转换并返回
        return categories.stream()
                .map(category -> {
                    // 创建类别帖子计数 dto
                    CategoryWithPostCountDTO categoryWithPostCountDTO = CategoryConvert.INSTANCE.convertPostCountDTO(category);

                    categoryWithPostCountDTO.setPostCount(categoryPostCountMap.getOrDefault(category.getId(), 0L));

                    StringBuilder fullPath = new StringBuilder();

                    if (optionService.isEnabledAbsolutePath()) {
                        fullPath.append(optionService.getBlogBaseUrl());
                    }

                    fullPath.append(URL_SEPARATOR)
                            .append(optionService.getCategoriesPrefix())
                            .append(URL_SEPARATOR)
                            .append(category.getSlug())
                            .append(optionService.getPathSuffix());

                    categoryWithPostCountDTO.setFullPath(fullPath.toString());

                    return categoryWithPostCountDTO;
                })
                .collect(Collectors.toList());
    }

//
//    @Override
//    public List<Post> listPostBy(Integer categoryId) {
//        Assert.notNull(categoryId, "Category id must not be null");
//
//        // Find all post ids
//        Set<Integer> postIds = postCategoryRepository.findAllPostIdsByCategoryId(categoryId);
//
//        return postRepository.findAllById(postIds);
//    }
//
//    @Override
//    public List<Post> listPostBy(Integer categoryId, PostStatus status) {
//        Assert.notNull(categoryId, "Category id must not be null");
//        Assert.notNull(status, "Post status must not be null");
//
//        // Find all post ids
//        Set<Integer> postIds = postCategoryRepository.findAllPostIdsByCategoryId(categoryId, status);
//
//        return postRepository.findAllById(postIds);
//    }
//
//    @Override
//    public List<Post> listPostBy(String slug, PostStatus status) {
//        Assert.notNull(slug, "Category slug must not be null");
//        Assert.notNull(status, "Post status must not be null");
//
//        Category category = categoryRepository.getBySlug(slug).orElseThrow(() -> new NotFoundException("查询不到该分类的信息").setErrorData(slug));
//
//        Set<Integer> postsIds = postCategoryRepository.findAllPostIdsByCategoryId(category.getId(), status);
//
//        return postRepository.findAllById(postsIds);
//    }
//
//    @Override
//    public Page<Post> pagePostBy(Integer categoryId, Pageable pageable) {
//        Assert.notNull(categoryId, "Category id must not be null");
//        Assert.notNull(pageable, "Page info must not be null");
//
//        // Find all post ids
//        Set<Integer> postIds = postCategoryRepository.findAllPostIdsByCategoryId(categoryId);
//
//        return postRepository.findAllByIdIn(postIds, pageable);
//    }
//
//    @Override
//    public Page<Post> pagePostBy(Integer categoryId, PostStatus status, Pageable pageable) {
//        Assert.notNull(categoryId, "Category id must not be null");
//        Assert.notNull(status, "Post status must not be null");
//        Assert.notNull(pageable, "Page info must not be null");
//
//        // Find all post ids
//        Set<Integer> postIds = postCategoryRepository.findAllPostIdsByCategoryId(categoryId, status);
//
//        return postRepository.findAllByIdIn(postIds, pageable);
//    }
//
//    @Override
//    public List<PostCategory> mergeOrCreateByIfAbsent(Integer postId, Set<Integer> categoryIds) {
//        Assert.notNull(postId, "Post id must not be null");
//
//        if (CollectionUtils.isEmpty(categoryIds)) {
//            return Collections.emptyList();
//        }
//
//        // Build post categories
//        List<PostCategory> postCategoriesStaging = categoryIds.stream().map(categoryId -> {
//            PostCategory postCategory = new PostCategory();
//            postCategory.setPostId(postId);
//            postCategory.setCategoryId(categoryId);
//            return postCategory;
//        }).collect(Collectors.toList());
//
//        List<PostCategory> postCategoriesToCreate = new LinkedList<>();
//        List<PostCategory> postCategoriesToRemove = new LinkedList<>();
//
//        // Find all exist post categories
//        List<PostCategory> postCategories = postCategoryRepository.findAllByPostId(postId);
//
//        postCategories.forEach(postCategory -> {
//            if (!postCategoriesStaging.contains(postCategory)) {
//                postCategoriesToRemove.add(postCategory);
//            }
//        });
//
//        postCategoriesStaging.forEach(postCategoryStaging -> {
//            if (!postCategories.contains(postCategoryStaging)) {
//                postCategoriesToCreate.add(postCategoryStaging);
//            }
//        });
//
//        // Remove post categories
//        removeAll(postCategoriesToRemove);
//
//        // Remove all post categories need to remove
//        postCategories.removeAll(postCategoriesToRemove);
//
//        // Add all created post categories
//        postCategories.addAll(createInBatch(postCategoriesToCreate));
//
//        // Create them
//        return postCategories;
//    }
//
//    @Override
//    public List<PostCategory> listByPostId(Integer postId) {
//        Assert.notNull(postId, "Post id must not be null");
//
//        return postCategoryRepository.findAllByPostId(postId);
//    }
//
//    @Override
//    public List<PostCategory> listByCategoryId(Integer categoryId) {
//        Assert.notNull(categoryId, "Category id must not be null");
//
//        return postCategoryRepository.findAllByCategoryId(categoryId);
//    }
//
//    @Override
//    public Set<Integer> listCategoryIdsByPostId(Integer postId) {
//        Assert.notNull(postId, "Post id must not be null");
//
//        return postCategoryRepository.findAllCategoryIdsByPostId(postId);
//    }
//
//    @Override
//    public List<PostCategory> removeByPostId(Integer postId) {
//        Assert.notNull(postId, "Post id must not be null");
//
//        return postCategoryRepository.deleteByPostId(postId);
//    }
//
//    @Override
//    public List<PostCategory> removeByCategoryId(Integer categoryId) {
//        Assert.notNull(categoryId, "Category id must not be null");
//
//        return postCategoryRepository.deleteByCategoryId(categoryId);
//    }
//
//    @Override
//    public List<CategoryWithPostCountDTO> listCategoryWithPostCountDto(Sort sort) {
//        Assert.notNull(sort, "Sort info must not be null");
//
//        List<Category> categories = categoryRepository.findAll(sort);
//
//        // Query category post count
//        Map<Integer, Long> categoryPostCountMap = ServiceUtils.convertToMap(postCategoryRepository.findPostCount(), CategoryPostCountProjection::getCategoryId, CategoryPostCountProjection::getPostCount);
//
//        // Convert and return
//        return categories.stream()
//                .map(category -> {
//                    // Create category post count dto
//                    CategoryWithPostCountDTO categoryWithPostCountDTO = new CategoryWithPostCountDTO().convertFrom(category);
//                    // Set post count
//                    categoryWithPostCountDTO.setPostCount(categoryPostCountMap.getOrDefault(category.getId(), 0L));
//
//                    StringBuilder fullPath = new StringBuilder();
//
//                    if (optionService.isEnabledAbsolutePath()) {
//                        fullPath.append(optionService.getBlogBaseUrl());
//                    }
//
//                    fullPath.append(URL_SEPARATOR)
//                            .append(optionService.getCategoriesPrefix())
//                            .append(URL_SEPARATOR)
//                            .append(category.getSlug())
//                            .append(optionService.getPathSuffix());
//
//                    categoryWithPostCountDTO.setFullPath(fullPath.toString());
//
//                    return categoryWithPostCountDTO;
//                })
//                .collect(Collectors.toList());
//    }
}
