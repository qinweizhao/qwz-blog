package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author qinweizhao
 * @since 2022/7/6
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {
}
