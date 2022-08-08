package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Objects;
import com.qinweizhao.blog.model.convert.CategoryConvert;
import com.qinweizhao.blog.exception.AlreadyExistsException;
import com.qinweizhao.blog.exception.NotFoundException;
import com.qinweizhao.blog.mapper.CategoryMapper;
import com.qinweizhao.blog.mapper.PostCategoryMapper;
import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.entity.Category;
import com.qinweizhao.blog.model.param.CategoryParam;
import com.qinweizhao.blog.service.CategoryService;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.util.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.qinweizhao.blog.model.support.HaloConst.URL_SEPARATOR;

/**
 * CategoryService implementation class.
 *
 * @author ryanwang
 * @author johnniang
 * @author qinweizhao
 * @date 2019-03-14
 */
@Slf4j
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    private final PostCategoryMapper postCategoryMapper;

    private final OptionService optionService;

    @Override
    public CategoryDTO getById(Integer categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        return CategoryConvert.INSTANCE.convert(category);
    }

    @Override
    public List<CategoryDTO> list() {
        return CategoryConvert.INSTANCE.convertToDTO(categoryMapper.selectList());
    }


    @Override
    public List<CategoryDTO> listAsTree() {

        List<Category> categories = categoryMapper.selectList();

        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }

        // Create top category
        CategoryDTO topLevelCategory = createTopLevelCategory();

        // Concrete the tree
        concreteTree(topLevelCategory, categories);

        return topLevelCategory.getChildren();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveCategory(CategoryParam param) {
        Category category = CategoryConvert.INSTANCE.convert(param);

        // 检查分类名字
        long count = categoryMapper.selectCountByName(category.getName());

        if (count > 0) {
            log.error("分类已存在: [{}]", category);
            throw new AlreadyExistsException("该分类已存在");
        }

        if (!ServiceUtils.isEmptyId(category.getParentId())) {
            count = categoryMapper.selectCountById(category.getParentId());

            if (count == 0) {
                log.error("父分类 ID : [{}] 没有找到, 分类: [{}]", category.getParentId(), category);
                throw new NotFoundException("没有分类 ID  = " + category.getParentId() + " 没有找到");
            }
        }

        return categoryMapper.insert(category) > 0;
    }

    @Override
    public boolean updateById(Integer categoryId, CategoryParam categoryParam) {
        Category category = CategoryConvert.INSTANCE.convert(categoryParam);
        category.setId(categoryId);
        return categoryMapper.updateById(category) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeCategoryAndPostCategoryById(Integer categoryId) {
        List<Category> categories = listByParentId(categoryId);
        if (null != categories && categories.size() > 0) {
            categories.forEach(category -> {
                category.setParentId(0);
                categoryMapper.updateById(category);
            });
        }
        // Remove category
        categoryMapper.deleteById(categoryId);
        // Remove post categories
        postCategoryMapper.deleteByCategoryId(categoryId);
        return true;
    }

    @Override
    public Long count() {
        return categoryMapper.selectCount(Wrappers.emptyWrapper());
    }


    private List<Category> listByParentId(Integer parentId) {
        Assert.notNull(parentId, "父分类 id 不能为空");
        return categoryMapper.selectListByParentId(parentId);
    }

    /**
     * 创建顶级分类
     *
     * @return top level category with id 0
     */
    private CategoryDTO createTopLevelCategory() {
        CategoryDTO topCategory = new CategoryDTO();
        // Set default value
        topCategory.setId(0);
        topCategory.setChildren(new LinkedList<>());
        topCategory.setParentId(-1L);

        return topCategory;
    }


    /**
     * 具体的分类树
     *
     * @param parentCategory parentCategory
     * @param categories     categories
     */
    private void concreteTree(CategoryDTO parentCategory, List<Category> categories) {
        Assert.notNull(parentCategory, "父分类不能为空");

        if (CollectionUtils.isEmpty(categories)) {
            return;
        }

        // Get children for removing after
        List<Category> children = categories.stream()
                .filter(category -> Objects.equal(parentCategory.getId(), category.getParentId()))
                .collect(Collectors.toList());

        children.forEach(category -> {
            // Convert to child category vo
            CategoryDTO child = CategoryConvert.INSTANCE.convert(category);
            // Init children if absent
            if (parentCategory.getChildren() == null) {
                parentCategory.setChildren(new LinkedList<>());
            }

            StringBuilder fullPath = new StringBuilder();

            if (optionService.isEnabledAbsolutePath()) {
                fullPath.append(optionService.getBlogBaseUrl());
            }

            fullPath.append(URL_SEPARATOR)
                    .append(optionService.getCategoriesPrefix())
                    .append(URL_SEPARATOR)
                    .append(child.getSlug())
                    .append(optionService.getPathSuffix());

            child.setFullPath(fullPath.toString());

            // Add child
            parentCategory.getChildren().add(child);
        });

        // Remove all child categories
        categories.removeAll(children);

        // Foreach children vos
        if (!CollectionUtils.isEmpty(parentCategory.getChildren())) {
            parentCategory.getChildren().forEach(childCategory -> concreteTree(childCategory, categories));
        }
    }


}
