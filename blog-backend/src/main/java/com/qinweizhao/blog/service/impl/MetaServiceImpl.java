package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.convert.MetaConvert;
import com.qinweizhao.blog.mapper.MetaMapper;
import com.qinweizhao.blog.model.dto.MetaDTO;
import com.qinweizhao.blog.model.entity.Meta;
import com.qinweizhao.blog.service.MetaService;
import com.qinweizhao.blog.util.ServiceUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author qinweizhao
 * @since 2022/7/14
 */
@Service
public class MetaServiceImpl extends ServiceImpl<MetaMapper, Meta> implements MetaService {


    @Override
    public Map<Integer, List<MetaDTO>> getListMetaAsMapByPostIds(Set<Integer> postIds) {
        List<MetaDTO> metas = MetaConvert.INSTANCE.convertToDTO(this.baseMapper.selectListByPostIds(postIds));
        // 转换结构
        Map<Long, MetaDTO> postMetaMap = ServiceUtils.convertToMap(metas, MetaDTO::getId);

        // 创建新的结构
        Map<Integer, List<MetaDTO>> postMetaListMap = new LinkedHashMap<>();

        // 寻找并收集
        metas.forEach(meta -> postMetaListMap.computeIfAbsent(meta.getPostId(), postId -> new LinkedList<>())
                .add(postMetaMap.get(meta.getId())));

        return postMetaListMap;
    }
}
