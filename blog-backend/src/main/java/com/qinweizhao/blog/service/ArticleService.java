package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ArticleDTO;
import com.qinweizhao.blog.model.dto.ArticleListDTO;
import com.qinweizhao.blog.model.dto.ArticleSimpleDTO;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import com.qinweizhao.blog.model.param.ArticleParam;
import com.qinweizhao.blog.model.param.ArticleQueryParam;
import com.qinweizhao.blog.model.vo.ArchiveMonthVO;
import com.qinweizhao.blog.model.vo.ArchiveYearVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Post service interface.
 *
 * @author qinweizhao
 * @since 2019-03-14
 */
public interface ArticleService {

    /**
     * 分页
     *
     * @param param param
     * @return PageResult
     */
    PageResult<ArticleListDTO> page(ArticleQueryParam param);


    /**
     * 分页
     *
     * @param param param
     * @return PageResult
     */
    PageResult<ArticleSimpleDTO> pageSimple(ArticleQueryParam param);

    /**
     * 详情
     *
     * @param postId postId
     * @return PostDetailDTO
     */
    ArticleDTO getById(Integer postId);


    /**
     * 详情
     *
     * @param postId postId
     * @return PostDetailDTO
     */
    ArticleSimpleDTO getSimpleById(Integer postId);


    /**
     * 生成描述
     *
     * @param content content
     * @return String
     */
    String generateDescription(String content);


    /**
     * 列表（最近更新）
     *
     * @param top top
     * @return List
     */
    List<ArticleSimpleDTO> listSimple(int top);

    /**
     * 新增
     *
     * @param param param
     * @return boolean
     */
    int save(ArticleParam param);

    /**
     * 更新
     *
     * @param postId postId
     * @param param  param
     * @return boolean
     */
    boolean update(Integer postId, ArticleParam param);

    /**
     * 统计文章个数
     *
     * @param published published
     * @return Long
     */
    long countByStatus(ArticleStatus published);

    /**
     * 阅读次数
     *
     * @return long
     */
    long countVisit();

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
    boolean updateStatus(ArticleStatus status, Integer postId);

    /**
     * 更新状态
     *
     * @param ids    ids
     * @param status status
     * @return boolean
     */
    boolean updateStatusByIds(List<Integer> ids, ArticleStatus status);

    /**
     * 获取文章状态
     *
     * @param postId postId
     * @return PostStatus
     */
    ArticleStatus getStatusById(Integer postId);

    /**
     * 获取上一个文章(发布状态)
     *
     * @param postId postId
     * @return PostDTO
     */
    ArticleDTO getPrevPost(Integer postId);

    /**
     * 获取下一个文章(发布状态)
     *
     * @param postId postId
     * @return PostDTO
     */
    ArticleDTO getNextPost(Integer postId);

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
     *
     * @param file file
     * @return boolean
     */
    boolean importMarkdown(MultipartFile file);
}
