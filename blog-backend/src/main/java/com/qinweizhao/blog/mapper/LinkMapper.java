package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.Link;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2022/7/5
 */
@Mapper
public interface LinkMapper extends BaseMapper<Link> {

    /**
     * 查询所有 teams
     *
     * @return List
     */
    List<String> selectListTeams();

    /**
     * 通过 name 和 id 判断是否存在
     *
     * @param name name
     * @param id   id
     * @return boolean
     */
    default boolean existsByNameAndIdNot(String name, Integer id) {
        return exists(new LambdaQueryWrapper<Link>()
                .eq(Link::getName, name)
                .eq(Link::getId, id));
    }


    /**
     * 通过 url 和 id 判断是否存在
     *
     * @param url url
     * @param id  id
     * @return boolean
     */
    default boolean existsByUrlAndIdNot(String url, Integer id) {
        return exists(new LambdaQueryWrapper<Link>()
                .eq(Link::getUrl, url)
                .eq(Link::getId, id));
    }
}
