package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.entity.Photo;
import com.qinweizhao.blog.model.param.PhotoQueryParam;
import com.qinweizhao.blog.util.LambdaQueryWrapperX;
import com.qinweizhao.blog.util.MyBatisUtils;
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

    /**
     * 分页
     *
     * @param param param
     * @return PageResult
     */
    default PageResult<Photo> selectPagePhotos(PhotoQueryParam param) {
        Page<Photo> page = MyBatisUtils.buildPage(param);
        Page<Photo> photoPage = this.selectPage(page, new LambdaQueryWrapperX<Photo>()
                .likeIfPresent(Photo::getName, param.getKeyword())
                .eqIfPresent(Photo::getTeam, param.getTeam())
        );
        return MyBatisUtils.buildPageResult(photoPage);
    }

    /**
     * 列表
     *
     * @param team team
     * @return List
     */
    default List<Photo> selectListByTeam(String team) {
        return this.selectList(new LambdaQueryWrapperX<Photo>()
                .eq(Photo::getTeam, team)
        );
    }

}
