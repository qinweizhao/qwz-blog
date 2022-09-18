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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Post service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @author guqing
 * @author qinweizhao
 * @since 2019-03-14
 */
public interface PostService {

    /**
     * 分页
     *
     * @param param param
     * @return PageResult
     */
    PageResult<PostListDTO> page(PostQueryParam param);


    /**
     * 分页
     *
     * @param param param
     * @return PageResult
     */
    PageResult<PostSimpleDTO> pageSimple(PostQueryParam param);

    /**
     * 详情
     *
     * @param postId postId
     * @return PostDetailDTO
     */
    PostDTO getById(Integer postId);


    /**
     * 详情
     *
     * @param postId postId
     * @return PostDetailDTO
     */
    PostSimpleDTO getSimpleById(Integer postId);


    /**
     * 生成描述
     *
     * @param content content
     * @return String
     */
    String generateDescription(String content);

    /**
     * 构建完整路径
     *
     * @param postId postId
     * @return String
     */
    String buildFullPath(Integer postId);


    /**
     * 列表（最新发布）
     *
     * @param top top
     * @return List
     */
    List<PostSimpleDTO> listSimple(int top);

    /**
     * 新增
     *
     * @param param param
     * @return boolean
     */
    int save(PostParam param);

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

    /**
     * 获取预览地址
     *
     * @param postId postId
     * @return String
     */
    String getPreviewUrl(Integer postId);

    /**
     * 删除
     *
     * @param postId postId
     * @return boolean
     */
    boolean removeById(Integer postId);

    /**
     * 批量删除
     *
     * @param postIds postIds
     * @return boolean
     */
    boolean removeByIds(List<Integer> postIds);

    /**
     * 更新草稿
     *
     * @param content content
     * @param postId  postId
     * @return boolean
     */
    boolean updateDraftContent(String content, Integer postId);

    /**
     * 更新状态
     *
     * @param status status
     * @param postId postId
     * @return boolean
     */
    boolean updateStatus(PostStatus status, Integer postId);

    /**
     * 更新状态
     *
     * @param ids    ids
     * @param status status
     * @return boolean
     */
    boolean updateStatusByIds(List<Integer> ids, PostStatus status);

    /**
     * 获取文章状态
     *
     * @param postId postId
     * @return PostStatus
     */
    PostStatus getStatusById(Integer postId);

    /**
     * 获取上一个文章(发布状态)
     *
     * @param postId postId
     * @return PostDTO
     */
    PostDTO getPrevPost(Integer postId);

    /**
     * 获取下一个文章(发布状态)
     *
     * @param postId postId
     * @return PostDTO
     */
    PostDTO getNextPost(Integer postId);

    /**
     * 统计个数
     *
     * @return long
     */
    long count();

    /**
     * 增加访问量
     *
     * @param postId postId
     * @return boolean
     */
    boolean increaseVisit(Integer postId);

    /**
     * 增加点赞
     *
     * @param postId postId
     * @return boolean
     */
    boolean increaseLike(Integer postId);

    /**
     * 发送访问事件
     *
     * @param postId postId
     */
    void publishVisitEvent(Integer postId);

    /**
     * 归档（Year）
     *
     * @return List
     */
    List<ArchiveYearVO> listYearArchives();


    /**
     * 归档（Month）
     *
     * @return List
     */
    List<ArchiveMonthVO> listMonthArchives();


    /**
     * 导入文章
     * @param file file
     * @return boolean
     */
    boolean importMarkdown(MultipartFile file);
}
