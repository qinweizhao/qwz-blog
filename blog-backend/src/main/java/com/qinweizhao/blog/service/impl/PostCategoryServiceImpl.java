package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.mapper.PostCategoryMapper;
import com.qinweizhao.blog.model.entity.Category;
import com.qinweizhao.blog.model.entity.PostCategory;
import com.qinweizhao.blog.service.CategoryService;
import com.qinweizhao.blog.service.PostCategoryService;
import com.qinweizhao.blog.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

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


    private final CategoryService categoryService;

    //
//    private final PostRepository postRepository;
//
//    private final CategoryRepository categoryRepository;
//
//    private final OptionService optionService;
//
//    @Override
//    public List<Category> listCategoriesBy(Integer postId) {
//        Assert.notNull(postId, "Post id must not be null");
//
//        // Find all category ids
//        Set<Integer> categoryIds = postCategoryRepository.findAllCategoryIdsByPostId(postId);
//
//        return categoryRepository.findAllById(categoryIds);
//    }
//
    @Override
    public Map<Integer, List<Category>> listCategoryListMap(Collection<Integer> postIds) {
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyMap();
        }

        // 查询所有关联关系
        List<PostCategory> postCategories = this.baseMapper.selectListByPostIds(postIds);

        // 获取分类 id
        Set<Integer> categoryIds = ServiceUtils.fetchProperty(postCategories, PostCategory::getCategoryId);

        // 查询所有分类
        List<Category> categories = categoryService.listByIds(categoryIds);

        // 转换为 map
        Map<Integer, Category> categoryMap = ServiceUtils.convertToMap(categories, Category::getId);

        // 创建新的结构
        Map<Integer, List<Category>> categoryListMap = new HashMap<>();

        // 查找并收集
        postCategories.forEach(postCategory -> categoryListMap.computeIfAbsent(postCategory.getPostId(), postId -> new LinkedList<>())
                .add(categoryMap.get(postCategory.getCategoryId())));

        return categoryListMap;
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
