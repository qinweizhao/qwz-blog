package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.PostDTO;
import com.qinweizhao.blog.model.dto.PostListDTO;
import com.qinweizhao.blog.model.dto.PostSimpleDTO;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.PostParam;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.model.vo.ArchiveMonthVO;
import com.qinweizhao.blog.model.vo.ArchiveYearVO;

import java.util.List;

/**
 * Post service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @author guqing
 * @date 2019-03-14
 */
public interface PostService {

    /**
     * 详情
     *
     * @param postId postId
     * @return PostDetailDTO
     */
    PostDTO getById(Integer postId);

    /**
     * 获取详情
     *
     * @param published published
     * @param slug      slug
     * @return PostDetailDTO
     */
    PostDTO getBySlugAndStatus(PostStatus published, String slug);

    /**
     * 更新状态
     *
     * @param status status
     * @param postId postId
     * @return boolean
     */
    boolean updateStatus(PostStatus status, Integer postId);

    /**
     * 生成描述
     *
     * @param content content
     * @return String
     */
    String generateDescription(String content);

    List<PostListDTO> convertToListVo(List<PostSimpleDTO> simpleDTOList);

    List<ArchiveYearVO> listYearArchives();

    List<ArchiveMonthVO> listMonthArchives();

    /**
     * 列表（最新发布）
     *
     * @param top top
     * @return List
     */
    List<PostSimpleDTO> listSimple(int top);

    /**
     * 分页
     *
     * @param param param
     * @return PageResult
     */
    PageResult<PostListDTO> page(PostQueryParam param);

    /**
     * 新增
     *
     * @param param param
     * @return boolean
     */
    boolean save(PostParam param);

    /**
     * 更新
     *
     * @param postId postId
     * @param param  param
     * @return boolean
     */
    boolean update(Integer postId, PostParam param);

    /**
     * 统计文章个数
     *
     * @param published published
     * @return Long
     */
    long countByStatus(PostStatus published);

    /**
     * 阅读次数
     *
     * @return long
     */
    long countVisit();

    /**
     * 阅读次数
     *
     * @return long
     */
    long countLike();

}
