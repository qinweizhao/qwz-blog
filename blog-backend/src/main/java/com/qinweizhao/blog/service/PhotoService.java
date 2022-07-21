package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.PhotoDTO;
import com.qinweizhao.blog.model.param.PhotoQueryParam;
import com.qinweizhao.blog.model.params.PhotoParam;

import java.util.List;

/**
 * Photo service interface.
 *
 * @author qinweizhao
 * @date 2019-03-14
 */
public interface PhotoService {


    /**
     * 列表
     *
     * @return List
     */
    List<PhotoDTO> list();

    /**
     * 分页
     *
     * @param param param
     * @return PageResult
     */
    PageResult<PhotoDTO> page(PhotoQueryParam param);

    /**
     * 详情
     *
     * @param photoId photoId
     * @return PhotoDTO
     */
    PhotoDTO getById(Integer photoId);

    /**
     * 移除
     *
     * @param photoId photoId
     * @return boolean
     */
    boolean removeById(Integer photoId);

    /**
     * 新增
     *
     * @param photoParam photoParam
     * @return boolean
     */
    boolean save(PhotoParam photoParam);

    /**
     * 更新
     *
     * @param photoId    photoId
     * @param photoParam photoParam
     * @return boolean
     */
    boolean updateById(Integer photoId, PhotoParam photoParam);

    /**
     * listTeams
     * @return List
     */
    List<String> listTeams();



//    /**
//     * List photo dtos.
//     *
//     * @param sort sort
//     * @return all photos
//     */
//    List<PhotoDTO> listDtos(@NonNull Sort sort);
//
//    /**
//     * Lists photo team vos.
//     *
//     * @param sort must not be null
//     * @return a list of photo team vo
//     */
//    List<PhotoTeamVO> listTeamVos(@NonNull Sort sort);
//
//    /**
//     * List photos by team.
//     *
//     * @param team team
//     * @param sort sort
//     * @return list of photos
//     */
//    List<PhotoDTO> listByTeam(@NonNull String team, Sort sort);
//
//    /**
//     * Pages photo output dtos.
//     *
//     * @param pageable page info must not be null
//     * @return a page of photo output dto
//     */
//    Page<PhotoDTO> pageBy(@NonNull Pageable pageable);
//
//    /**
//     * Pages photo output dtos.
//     *
//     * @param pageable   page info must not be null
//     * @param photoQuery photoQuery
//     * @return a page of photo output dto
//     */
//    @NonNull
//    Page<PhotoDTO> pageDtosBy(@NonNull Pageable pageable, PhotoQuery photoQuery);
//
//    /**
//     * Creates photo by photo param.
//     *
//     * @param photoParam must not be null
//     * @return create photo
//     */
//    @NonNull
//    Photo createBy(@NonNull PhotoParam photoParam);
//

//    /**
//     * List all teams.
//     *
//     * @return list of teams
//     */
//    List<String> listAllTeams();
//
//    /**
//     * Replace photo url in batch.
//     *
//     * @param oldUrl old blog url.
//     * @param newUrl new blog url.
//     * @return replaced photos.
//     */
//    List<PhotoDTO> replaceUrl(@NonNull String oldUrl, @NonNull String newUrl);
}
