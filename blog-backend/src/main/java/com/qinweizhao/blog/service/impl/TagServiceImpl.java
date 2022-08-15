package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qinweizhao.blog.exception.AlreadyExistsException;
import com.qinweizhao.blog.mapper.PostTagMapper;
import com.qinweizhao.blog.mapper.TagMapper;
import com.qinweizhao.blog.model.convert.TagConvert;
import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.entity.Tag;
import com.qinweizhao.blog.model.param.TagParam;
import com.qinweizhao.blog.model.projection.TagPostPostCountProjection;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.PostTagService;
import com.qinweizhao.blog.service.TagService;
import com.qinweizhao.blog.util.ServiceUtils;
import com.qinweizhao.blog.util.SlugUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.qinweizhao.blog.model.support.HaloConst.URL_SEPARATOR;

/**
 * TagService implementation class.
 *
 * @author johnniang
 * @author ryanwang
 * @since 2019-03-14
 */
@Slf4j
@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {


    private final ConfigService configService;

    private final PostTagMapper postTagMapper;

    private final PostTagService postTagService;

    private final TagMapper tagMapper;


    @Override
    public List<TagDTO> list() {
        List<Tag> tags = tagMapper.selectList(Wrappers.emptyWrapper());
        List<TagDTO> result = TagConvert.INSTANCE.convertToDTO(tags);


        // 查找所有帖子计数
        Map<Integer, Long> tagPostCountMap = ServiceUtils.convertToMap(postTagMapper.selectPostCount(), TagPostPostCountProjection::getTagId, TagPostPostCountProjection::getPostCount);

        result.forEach(item->{
            item.setPostCount(tagPostCountMap.getOrDefault(item.getId(), 0L));

            StringBuilder fullPath = new StringBuilder();

            if (configService.isEnabledAbsolutePath()) {
                fullPath.append(configService.getBlogBaseUrl());
            }

            fullPath.append(URL_SEPARATOR)
                    .append(configService.getTagsPrefix())
                    .append(URL_SEPARATOR)
                    .append(item.getSlug())
                    .append(configService.getPathSuffix());

            item.setFullPath(fullPath.toString());


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

        StringBuilder fullPath = new StringBuilder();

        if (configService.isEnabledAbsolutePath()) {
            fullPath.append(configService.getBlogBaseUrl());
        }

        fullPath.append(URL_SEPARATOR)
                .append(configService.getTagsPrefix())
                .append(URL_SEPARATOR)
                .append(tag.getSlug())
                .append(configService.getPathSuffix());

        tagDTO.setFullPath(fullPath.toString());

        return tagDTO;
    }


    @Override
    public boolean updateById(Integer tagId, TagParam tagParam) {
        Tag tag = TagConvert.INSTANCE.convert(tagParam);
        tag.setId(tagId);
        return tagMapper.updateById(tag) > 0;
    }

    @Override
    public boolean removeById(Integer tagId) {

        int i = tagMapper.deleteById(tagId);

        postTagService.removeByTagId(tagId);

        return i > 0;
    }

    @Override
    public Long count() {
        return tagMapper.selectCount(null);
    }


}
