package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.Photo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2022/7/6
 */
@Mapper
public interface PhotoMapper extends BaseMapper<Photo> {

//    /**
//     * 按 team 查询照片
//     *
//     * @param team team
//     * @param sort sort
//     * @return List
//     */
//    default List<Photo> selectByTeam(String team, Sort sort) {
//        return selectList(new LambdaQueryWrapper<Photo>()
//                .eq(Photo::getTeam, team));
//    }

    /**
     * 查找所有 teams
     *
     * @return List
     */
    List<String> selectListTeam();

}
