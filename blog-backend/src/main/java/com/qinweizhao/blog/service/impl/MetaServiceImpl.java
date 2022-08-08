package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.model.convert.MetaConvert;
import com.qinweizhao.blog.mapper.MetaMapper;
import com.qinweizhao.blog.model.dto.MetaDTO;
import com.qinweizhao.blog.model.entity.Meta;
import com.qinweizhao.blog.service.MetaService;
import com.qinweizhao.blog.util.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author qinweizhao
 * @since 2022/7/14
 */
@Service
@AllArgsConstructor
public class MetaServiceImpl extends ServiceImpl<MetaMapper, Meta> implements MetaService {

    private final MetaMapper metaMapper;

    @Override
    public Map<Integer, List<MetaDTO>> getListMetaAsMapByPostIds(Set<Integer> postIds) {
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyMap();
        }
        List<MetaDTO> metas = MetaConvert.INSTANCE.convertToDTO(metaMapper.selectListByPostIds(postIds));
        // 转换结构
        Map<Long, MetaDTO> postMetaMap = ServiceUtils.convertToMap(metas, MetaDTO::getId);

        // 创建新的结构
        Map<Integer, List<MetaDTO>> postMetaListMap = new LinkedHashMap<>();

        // 寻找并收集
        metas.forEach(meta -> postMetaListMap.computeIfAbsent(meta.getPostId(), postId -> new LinkedList<>())
                .add(postMetaMap.get(meta.getId())));

        return postMetaListMap;
    }

    @Override
    public boolean removeByPostId(Integer postId) {
        return metaMapper.deleteByPostId(postId);
    }
}
