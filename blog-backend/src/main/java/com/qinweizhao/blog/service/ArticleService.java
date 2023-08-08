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
     * @param articleId articleId
     * @return PostDetailDTO
     */
    ArticleDTO getById(Integer articleId);


    /**
     * 详情
     *
     * @param articleId articleId
     * @return PostDetailDTO
     */
    ArticleSimpleDTO getSimpleById(Integer articleId);


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
     * @param articleId articleId
     * @param param  param
     * @return boolean
     */
    boolean update(Integer articleId, ArticleParam param);

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
     * @param articleId articleId
     * @return String
     */
    String getPreviewUrl(Integer articleId);

    /**
     * 删除
     *
     * @param articleId articleId
     * @return boolean
     */
    boolean removeById(Integer articleId);

    /**
     * 批量删除
     *
     * @param articleIds articleIds
     * @return boolean
     */
    boolean removeByIds(List<Integer> articleIds);

    /**
     * 更新草稿
     *
     * @param content content
     * @param articleId  articleId
     * @return boolean
     */
    boolean updateDraftContent(String content, Integer articleId);

    /**
     * 更新状态
     *
     * @param status status
     * @param articleId articleId
     * @return boolean
     */
    boolean updateStatus(ArticleStatus status, Integer articleId);

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
     * @param articleId articleId
     * @return PostStatus
     */
    ArticleStatus getStatusById(Integer articleId);

    /**
     * 获取上一个文章(发布状态)
     *
     * @param articleId articleId
     * @return PostDTO
     */
    ArticleDTO getPrevPost(Integer articleId);

    /**
     * 获取下一个文章(发布状态)
     *
     * @param articleId articleId
     * @return PostDTO
     */
    ArticleDTO getNextPost(Integer articleId);

    /**
     * 统计个数
     *
     * @return long
     */
    long count();

    /**
     * 增加访问量
     *
     * @param articleId articleId
     * @return boolean
     */
    boolean increaseVisit(Integer articleId);

    /**
     * 发送访问事件
     *
     * @param articleId articleId
     */
    void publishVisitEvent(Integer articleId);

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
