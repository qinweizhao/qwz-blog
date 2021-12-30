package com.qinweizhao.site.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import com.qinweizhao.site.model.dto.PhotoDTO;
import com.qinweizhao.site.model.entity.Photo;
import com.qinweizhao.site.model.params.PhotoParam;
import com.qinweizhao.site.model.params.PhotoQuery;
import com.qinweizhao.site.model.vo.PhotoTeamVO;
import com.qinweizhao.site.service.base.CrudService;

/**
 * Photo service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-14
 */
public interface PhotoService extends CrudService<Photo, Integer> {

    /**
     * List photo dtos.
     *
     * @param sort sort
     * @return all photos
     */
    List<PhotoDTO> listDtos(@NonNull Sort sort);

    /**
     * Lists photo team vos.
     *
     * @param sort must not be null
     * @return a list of photo team vo
     */
    List<PhotoTeamVO> listTeamVos(@NonNull Sort sort);

    /**
     * List photos by team.
     *
     * @param team team
     * @param sort sort
     * @return list of photos
     */
    List<PhotoDTO> listByTeam(@NonNull String team, Sort sort);

    /**
     * Pages photo output dtos.
     *
     * @param pageable page info must not be null
     * @return a page of photo output dto
     */
    Page<PhotoDTO> pageBy(@NonNull Pageable pageable);

    /**
     * Pages photo output dtos.
     *
     * @param pageable page info must not be null
     * @param photoQuery photoQuery
     * @return a page of photo output dto
     */
    @NonNull
    Page<PhotoDTO> pageDtosBy(@NonNull Pageable pageable, PhotoQuery photoQuery);

    /**
     * Creates photo by photo param.
     *
     * @param photoParam must not be null
     * @return create photo
     */
    @NonNull
    Photo createBy(@NonNull PhotoParam photoParam);

    /**
     * List all teams.
     *
     * @return list of teams
     */
    List<String> listAllTeams();
}
