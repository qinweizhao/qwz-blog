package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qinweizhao.blog.exception.AlreadyExistsException;
import com.qinweizhao.blog.mapper.ArticleTagMapper;
import com.qinweizhao.blog.mapper.TagMapper;
import com.qinweizhao.blog.model.constant.SystemConstant;
import com.qinweizhao.blog.model.convert.TagConvert;
import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.entity.Tag;
import com.qinweizhao.blog.model.param.TagParam;
import com.qinweizhao.blog.model.projection.TagPostPostCountProjection;
import com.qinweizhao.blog.service.SettingService;
import com.qinweizhao.blog.service.ArticleTagService;
import com.qinweizhao.blog.service.TagService;
import com.qinweizhao.blog.util.ServiceUtils;
import com.qinweizhao.blog.util.SlugUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.qinweizhao.blog.model.support.BlogConst.URL_SEPARATOR;

/**
 * TagService implementation class.
 *
 * @since 2019-03-14
 */
@Slf4j
@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {


    private final SettingService settingService;

    private final ArticleTagMapper articleTagMapper;

    private final ArticleTagService articleTagService;

    private final TagMapper tagMapper;


    @Override
    public List<TagDTO> list() {
        List<Tag> tags = tagMapper.selectList(Wrappers.emptyWrapper());
        List<TagDTO> result = TagConvert.INSTANCE.convertToDTO(tags);


        // 查找所有帖子计数
        Map<Integer, Long> tagPostCountMap = ServiceUtils.convertToMap(articleTagMapper.selectPostCount(), TagPostPostCountProjection::getTagId, TagPostPostCountProjection::getPostCount);

        result.forEach(item -> {
            item.setPostCount(tagPostCountMap.getOrDefault(item.getId(), 0L));

            String fullPath = settingService.getBlogBaseUrl() + URL_SEPARATOR + SystemConstant.TAGS_PREFIX + URL_SEPARATOR + item.getSlug();

            item.setFullPath(fullPath);


        });

        return result;
    }

    @Override
    public boolean save(TagParam param) {

        String name = param.getName();
        // 检查标签是否存在
        long count = tagMapper.countByNameOrSlug(name, param.getSlug());

        log.debug("标签个数: [{}]", count);

        if (count > 0) {
            throw new AlreadyExistsException("该标签已存在").setErrorData(param);
        }

        Tag tag = TagConvert.INSTANCE.convert(param);

        String slug = param.getSlug();
        slug = StringUtils.isBlank(slug) ? SlugUtils.slug(name) : SlugUtils.slug(slug);

        tag.setSlug(slug);

        return tagMapper.insert(tag) > 0;
    }

    @Override
    public TagDTO getById(Integer tagId) {

        Tag tag = tagMapper.selectById(tagId);

        TagDTO tagDTO = TagConvert.INSTANCE.convert(tag);

        String fullPath = settingService.getBlogBaseUrl() + URL_SEPARATOR + SystemConstant.TAGS_PREFIX + URL_SEPARATOR + tag.getSlug();

        tagDTO.setFullPath(fullPath);

        return tagDTO;
    }

    @Override
    public TagDTO getBySlug(String slug) {
        Tag tag = tagMapper.selectBySlug(slug);
        return TagConvert.INSTANCE.convert(tag);
    }


    @Override
    public boolean updateById(Integer tagId, TagParam tagParam) {
        Tag tag = TagConvert.INSTANCE.convert(tagParam);
        tag.setId(tagId);
        return tagMapper.updateById(tag) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Integer tagId) {

        int i = tagMapper.deleteById(tagId);

        boolean b = articleTagService.removeByTagId(tagId);

        log.debug("删除文章与标签的关联结果{}", b);
        return i > 0;
    }

    @Override
    public Long count() {
        return tagMapper.selectCount(null);
    }


}
