//package com.qinweizhao.blog.service.impl;
//
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.google.common.base.Objects;
//import com.qinweizhao.blog.convert.CategoryConvert;
//import com.qinweizhao.blog.exception.AlreadyExistsException;
//import com.qinweizhao.blog.exception.NotFoundException;
//import com.qinweizhao.blog.mapper.CategoryMapper;
//import com.qinweizhao.blog.model.entity.Category;
//import com.qinweizhao.blog.model.vo.CategoryVO;
//import com.qinweizhao.blog.service.CategoryService;
//import com.qinweizhao.blog.service.OptionService;
//import com.qinweizhao.blog.service.PostCategoryService;
//import com.qinweizhao.blog.utils.ServiceUtils;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Sort;
//import org.springframework.lang.NonNull;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.Assert;
//import org.springframework.util.CollectionUtils;
//
//import java.util.Collections;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static com.qinweizhao.blog.model.support.HaloConst.URL_SEPARATOR;
//
///**
// * CategoryService implementation class.
// *
// * @author ryanwang
// * @author johnniang
// * @date 2019-03-14
// */
//@Slf4j
//@Service
//@AllArgsConstructor
//public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
//
//    private final PostCategoryService postCategoryService;
//
//    private final OptionService optionService;
//
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean saveCategory(Category category) {
//        Assert.notNull(category, "要创建的类别不能为空");
//
//        // Check the category name
//        long count = this.baseMapper.selectCountByName(category.getName());
//
//        if (count > 0) {
//            log.error("Category has exist already: [{}]", category);
//            throw new AlreadyExistsException("该分类已存在");
//        }
//
//        // Check parent id
//        if (!ServiceUtils.isEmptyId(category.getParentId())) {
//            count = this.baseMapper.selectCountById(category.getParentId());
//
//            if (count == 0) {
//                log.error("Parent category with id: [{}] was not found, category: [{}]", category.getParentId(), category);
//                throw new NotFoundException("Parent category with id = " + category.getParentId() + " was not found");
//            }
//        }
//
//        // Create it
//        return this.baseMapper.insert(category) > 0;
//    }
//
//    @Override
//    public List<Category> list() {
//        return this.baseMapper.selectList();
//    }
//
//    @Override
//    public List<CategoryVO> listAsTree(Sort sort) {
//        Assert.notNull(sort, "Sort info must not be null");
//
//        // List all category
////        List<Category> categories = list(sort);
//        List<Category> categories = list();
//
//        if (CollectionUtils.isEmpty(categories)) {
//            return Collections.emptyList();
//        }
//
//        // Create top category
//        CategoryVO topLevelCategory = createTopLevelCategory();
//
//        // Concrete the tree
//        concreteTree(topLevelCategory, categories);
//
//        return topLevelCategory.getChildren();
//    }
//
//    /**
//     * Concrete category tree.
//     *
//     * @param parentCategory parent category vo must not be null
//     * @param categories     a list of category
//     */
//    private void concreteTree(CategoryVO parentCategory, List<Category> categories) {
//        Assert.notNull(parentCategory, "Parent category must not be null");
//
//        if (CollectionUtils.isEmpty(categories)) {
//            return;
//        }
//
//        // Get children for removing after
//        List<Category> children = categories.stream()
//                .filter(category -> Objects.equal(parentCategory.getId(), category.getParentId()))
//                .collect(Collectors.toList());
//
//        children.forEach(category -> {
//            // Convert to child category vo
//            CategoryVO child = CategoryConvert.INSTANCE.convertVO(category);
//            // Init children if absent
//            if (parentCategory.getChildren() == null) {
//                parentCategory.setChildren(new LinkedList<>());
//            }
//
//            StringBuilder fullPath = new StringBuilder();
//
//            if (optionService.isEnabledAbsolutePath()) {
//                fullPath.append(optionService.getBlogBaseUrl());
//            }
//
//            fullPath.append(URL_SEPARATOR)
//                    .append(optionService.getCategoriesPrefix())
//                    .append(URL_SEPARATOR)
//                    .append(child.getSlug())
//                    .append(optionService.getPathSuffix());
//
//            child.setFullPath(fullPath.toString());
//
//            // Add child
//            parentCategory.getChildren().add(child);
//        });
//
//        // Remove all child categories
//        categories.removeAll(children);
//
//        // Foreach children vos
//        if (!CollectionUtils.isEmpty(parentCategory.getChildren())) {
//            parentCategory.getChildren().forEach(childCategory -> concreteTree(childCategory, categories));
//        }
//    }
//
//    /**
//     * Creates a top level category.
//     *
//     * @return top level category with id 0
//     */
//    @NonNull
//    private CategoryVO createTopLevelCategory() {
//        CategoryVO topCategory = new CategoryVO();
//        // Set default value
//        topCategory.setId(0);
//        topCategory.setChildren(new LinkedList<>());
//        topCategory.setParentId(-1);
//
//        return topCategory;
//    }
//
//    @Override
//    public Category getBySlug(String slug) {
//        return this.baseMapper.selectBySlug(slug);
//    }
//
//
//    @Override
//    public Category getByName(String name) {
//        return this.baseMapper.selectByName(name);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void removeCategoryAndPostCategoryBy(Integer categoryId) {
//        List<Category> categories = listByParentId(categoryId);
//        if (null != categories && categories.size() > 0) {
//            categories.forEach(category -> {
//                category.setParentId(0);
//                this.baseMapper.updateById(category);
//            });
//        }
//        // Remove category
//        removeById(categoryId);
//        // Remove post categories
//        postCategoryService.removeByCategoryId(categoryId);
//    }
//
//    @Override
//    public List<Category> listByParentId(Integer parentId) {
//        Assert.notNull(parentId, "Parent id must not be null");
//        return this.baseMapper.selectListByParentId(parentId);
//    }
//
//}
