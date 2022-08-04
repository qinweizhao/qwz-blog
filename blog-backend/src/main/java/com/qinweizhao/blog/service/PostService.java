package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.PostDTO;
import com.qinweizhao.blog.model.dto.PostSimpleDTO;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.model.vo.ArchiveMonthVO;
import com.qinweizhao.blog.model.vo.ArchiveYearVO;
import com.qinweizhao.blog.model.vo.PostVO;
import com.qinweizhao.blog.model.vo.PostListVO;

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
     * 统计文章个数
     * @param published published
     * @return Long
     */
    long countByStatus(PostStatus published);

    /**
     * 阅读次数
     * @return long
     */
    long countVisit();


    /**
     * 阅读次数
     * @return long
     */
    long countLike();

    /**
     * 分页
     * @param postQueryParam postQueryParam
     * @return PageResult
     */
    PageResult<PostSimpleDTO> pagePosts(PostQueryParam postQueryParam);


    /**
     *
     * @param postPage postPage
     * @return PageResult
     */
    PageResult<PostListVO> buildPostListVO(PageResult<PostSimpleDTO> postPage);


    /**
     * 更新状态
     * @param status status
     * @param postId postId
     * @return boolean
     */
    boolean updateStatus(PostStatus status, Integer postId);

    /**
     * 详情
     * @param postId postId
     * @return PostDetailDTO
     */
    PostDTO getById(Integer postId);


    PostVO convertToDetailVo(PostDTO postDetail);


    /**
     * 获取详情
     * @param published published
     * @param slug slug
     * @return PostDetailDTO
     */
    PostDTO getBySlugAndStatus(PostStatus published, String slug);

    /**
     * 生成描述
     * @param content content
     * @return
     */
    String generateDescription(String content);

    List<PostListVO> convertToListVo(List<PostSimpleDTO> simpleDTOList);

    List<ArchiveYearVO> listYearArchives();

    List<ArchiveMonthVO> listMonthArchives();


//
//    /**
//     * Pages posts.
//     *
//     * @param postQuery post query must not be null
//     * @param pageable  page info must not be null
//     * @return a page of post
//     */
//
//    Page<Post> pageBy( PostQuery postQuery,  Pageable pageable);
//
//    /**
//     * Pages post by keyword
//     *
//     * @param keyword  keyword
//     * @param pageable pageable
//     * @return a page of post
//     */
//
//    Page<Post> pageBy( String keyword,  Pageable pageable);
//
//    /**
//     * Creates post by post param.
//     *
//     * @param post        post must not be null
//     * @param tagIds      tag id set
//     * @param categoryIds category id set
//     * @param metas       metas
//     * @param autoSave    autoSave
//     * @return post created
//     */
//
////    PostDetailVO createBy( Post post, Set<Integer> tagIds, Set<Integer> categoryIds, Set<PostMeta> metas, boolean autoSave);
//
//    /**
//     * Creates post by post param.
//     *
//     * @param post        post must not be null
//     * @param tagIds      tag id set
//     * @param categoryIds category id set
//     * @param autoSave    autoSave
//     * @return post created
//     */
//
//    PostDetailVO createBy( Post post, Set<Integer> tagIds, Set<Integer> categoryIds, boolean autoSave);
//
//    /**
//     * Updates post by post, tag id set and category id set.
//     *
//     * @param postToUpdate post to update must not be null
//     * @param tagIds       tag id set
//     * @param categoryIds  category id set
//     * @param metas        metas
//     * @param autoSave     autoSave
//     * @return updated post
//     */
//
////    PostDetailVO updateBy( Post postToUpdate, Set<Integer> tagIds, Set<Integer> categoryIds, Set<PostMeta> metas, boolean autoSave);
//
//    /**
//     * Gets post by post status and slug.
//     *
//     * @param status post status must not be null
//     * @param slug   post slug must not be blank
//     * @return post info
//     */
//    Post getBy( PostStatus status,  String slug);

//    /**
//     * Gets post by post year and month and slug.
//     *
//     * @param year  post create year.
//     * @param month post create month.
//     * @param slug  post slug.
//     * @return post info
//     */
//
//    Post getBy( Integer year,  Integer month,  String slug);
//
//    /**
//     * Gets post by post year and slug.
//     *
//     * @param year  post create year.
//     * @param slug  post slug.
//     * @return post info
//     */
//
//    Post getBy( Integer year,  String slug);
//
//    /**
//     * Gets post by post year and month and slug.
//     *
//     * @param year   post create year.
//     * @param month  post create month.
//     * @param slug   post slug.
//     * @param status post status.
//     * @return post info
//     */
//
//    Post getBy( Integer year,  Integer month,  String slug,  PostStatus status);
//
//    /**
//     * Gets post by post year and month and slug.
//     *
//     * @param year  post create year.
//     * @param month post create month.
//     * @param day   post create day.
//     * @param slug  post slug.
//     * @return post info
//     */
//
//    Post getBy( Integer year,  Integer month,  Integer day,  String slug);
//
//    /**
//     * Gets post by post year and month and slug.
//     *
//     * @param year   post create year.
//     * @param month  post create month.
//     * @param day    post create day.
//     * @param slug   post slug.
//     * @param status post status.
//     * @return post info
//     */
//
//    Post getBy( Integer year,  Integer month,  Integer day,  String slug,  PostStatus status);
//
//
//
//    /**
//     * Lists year archives.
//     *
//     * @return a list of year archive
//     */
//
//    List<ArchiveYearVO> listYearArchives();
//
//    /**
//     * Lists month archives.
//     *
//     * @return a list of month archive
//     */
//
//    List<ArchiveMonthVO> listMonthArchives();
//
//    /**
//     * Convert to year archives
//     *
//     * @param posts posts must not be null
//     * @return list of ArchiveYearVO
//     */
//    List<ArchiveYearVO> convertToYearArchives( List<Post> posts);
//
//    /**
//     * Convert to month archives
//     *
//     * @param posts posts must not be null
//     * @return list of ArchiveMonthVO
//     */
//    List<ArchiveMonthVO> convertToMonthArchives( List<Post> posts);
//
//    /**
//     * Import post from markdown document.
//     *
//     * @param markdown markdown document.
//     * @param filename filename
//     * @return imported post
//     */
//
//    PostDetailVO importMarkdown( String markdown, String filename);
//
//    /**
//     * Export post to markdown file by post id.
//     *
//     * @param id post id
//     * @return markdown file content
//     */
//
//    String exportMarkdown( Integer id);
//
//    /**
//     * Export post to markdown file by post.
//     *
//     * @param post current post
//     * @return markdown file content
//     */
//
//    String exportMarkdown( Post post);
//
//    /**
//     * Converts to detail vo.
//     *
//     * @param post post must not be null
//     * @return post detail vo
//     */
//
//    PostDetailVO convertToDetailVo( Post post);
//
//    /**
//     * Converts to a page of post list vo.
//     *
//     * @param postPage post page must not be null
//     * @return a page of post list vo
//     */
//
//    Page<PostListVO> convertToListVo( Page<Post> postPage);
//
//    /**
//     * Converts to a list of post list vo.
//     *
//     * @param posts post must not be null
//     * @return a list of post list vo
//     */
//
//    List<PostListVO> convertToListVo( List<Post> posts);
//
//    /**
//     * Converts to a page of detail vo.
//     *
//     * @param postPage post page must not be null
//     * @return a page of post detail vo
//     */
//    Page<PostDetailVO> convertToDetailVo( Page<Post> postPage);
//
//    /**
//     * Publish a post visit event.
//     *
//     * @param postId postId must not be null
//     */
//    void publishVisitEvent(Integer postId);
//
//    /**
//     * Get Post Pageable default sort
//     *
//     * @return
//     * @Desc contains three parts. First, Top Priority; Second, From Custom index sort; Third, basic id sort
//     */
//    Sort getPostDefaultSort();
//
//    /**
//     *
//     * @param slug
//     * @return
//     */
//    Post getBySlug(String slug);
}
