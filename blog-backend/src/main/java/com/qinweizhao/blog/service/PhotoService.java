package com.qinweizhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.blog.model.entity.Photo;

import java.util.List;

/**
 * Photo service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-14
 */
public interface PhotoService extends IService<Photo> {

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
