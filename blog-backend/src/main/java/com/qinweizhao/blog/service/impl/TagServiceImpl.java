package com.qinweizhao.blog.service.impl;

import com.qinweizhao.blog.convert.TagConvert;
import com.qinweizhao.blog.exception.AlreadyExistsException;
import com.qinweizhao.blog.mapper.TagMapper;
import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.entity.Tag;
import com.qinweizhao.blog.model.param.TagParam;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.PostTagService;
import com.qinweizhao.blog.service.TagService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.qinweizhao.blog.model.support.HaloConst.URL_SEPARATOR;

/**
 * TagService implementation class.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-14
 */
@Slf4j
@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {


    private final OptionService optionService;

    private final PostTagService postTagService;

    private final TagMapper tagMapper;


    @Override
    public List<TagDTO> list() {
        List<Tag> tags = tagMapper.selectList(null);
        return TagConvert.INSTANCE.convertToDTO(tags);
    }

    @Override
    public boolean save(TagParam tagParam) {
        // 检查标签是否存在
        long count = tagMapper.countByNameOrSlug(tagParam.getName(), tagParam.getSlug());

        log.debug("标签个数: [{}]", count);

        if (count > 0) {
            throw new AlreadyExistsException("该标签已存在").setErrorData(tagParam);
        }

        Tag tag = TagConvert.INSTANCE.convert(tagParam);

        return tagMapper.insert(tag) > 0;
    }

    @Override
    public TagDTO getById(Integer tagId) {

        Tag tag = tagMapper.selectById(tagId);

        TagDTO tagDTO = TagConvert.INSTANCE.convert(tag);

        StringBuilder fullPath = new StringBuilder();

        if (optionService.isEnabledAbsolutePath()) {
            fullPath.append(optionService.getBlogBaseUrl());
        }

        fullPath.append(URL_SEPARATOR)
                .append(optionService.getTagsPrefix())
                .append(URL_SEPARATOR)
                .append(tag.getSlug())
                .append(optionService.getPathSuffix());

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
