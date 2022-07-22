package com.qinweizhao.blog.controller.admin;

import com.qinweizhao.blog.convert.CommentConvert;
import com.qinweizhao.blog.convert.JournalConvert;
import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.dto.JournalDTO;
import com.qinweizhao.blog.model.entity.Journal;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.model.vo.JournalCommentWithJournalVO;
import com.qinweizhao.blog.service.CommentService;
import com.qinweizhao.blog.service.JournalService;
import com.qinweizhao.blog.util.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 日志评论
 *
 * @author johnniang
 * @author qinweizhao
 * @date 2019-04-25
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/journals/comments")
public class JournalCommentController {

    private final CommentService commentService;

    private final JournalService journalService;


    /**
     * 分页
     *
     * @param param param
     * @return PageResult
     */
    @GetMapping
    public PageResult<JournalCommentWithJournalVO> page(CommentQueryParam param) {
        param.setType(CommentType.JOURNAL.getValue());

        PageResult<CommentDTO> commentResult = commentService.pageComment(param);

        List<CommentDTO> contents = commentResult.getContent();

        return new PageResult<>(this.buildResultVO(contents), commentResult.getTotal(),commentResult.hasPrevious(),commentResult.hasNext());
    }

    /**
     * 列出最新的日志评论
     *
     * @param top    top
     * @param status status
     * @return List
     */
    @GetMapping("latest")
    public List<JournalCommentWithJournalVO> listLatest(@RequestParam(name = "top", defaultValue = "10") int top,
                                                        @RequestParam(name = "status", required = false) CommentStatus status) {
        CommentQueryParam param = new CommentQueryParam();
        param.setPage(top);
        param.setStatus(status);

        List<CommentDTO> commentResult = commentService.pageComment(param).getContent();

        return this.buildResultVO(commentResult);
    }

    /**
     * 构建返回的VO
     *
     * @param contents contents
     * @return List
     */
    private List<JournalCommentWithJournalVO> buildResultVO(List<CommentDTO> contents) {
        // 获取 id
        Set<Integer> journalIds = ServiceUtils.fetchProperty(contents, CommentDTO::getPostId);

        if (ObjectUtils.isEmpty(journalIds)) {
            return new ArrayList<>();
        }

        Map<Integer, Journal> journalMap = ServiceUtils.convertToMap(journalService.listByIds(journalIds), Journal::getId);

        return contents.stream()
                .filter(comment -> journalMap.containsKey(comment.getPostId()))
                .map(comment -> {

                    JournalCommentWithJournalVO journalCommentWithJournalVO = CommentConvert.INSTANCE.convertJournalToVO(comment);

                    Journal journal = journalMap.get(comment.getPostId());
                    JournalDTO journalDTO = JournalConvert.INSTANCE.convert(journal);

                    journalCommentWithJournalVO.setJournal(journalDTO);

                    return journalCommentWithJournalVO;
                }).collect(Collectors.toList());
    }

//
//    @GetMapping("{journalId:\\d+}/tree_view")
//    @ApiOperation("Lists comments with tree view")
//    public Page<BaseCommentVO> listCommentTree(@PathVariable("journalId") Integer journalId,
//                                               @RequestParam(name = "page", required = false, defaultValue = "0") int page,
//                                               @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
//        return journalCommentService.pageVosAllBy(journalId, PageRequest.of(page, optionService.getCommentPageSize(), sort));
//    }
//
//    @GetMapping("{journalId:\\d+}/list_view")
//    @ApiOperation("Lists comment with list view")
//    public Page<BaseCommentWithParentVO> listComments(@PathVariable("journalId") Integer journalId,
//                                                      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
//                                                      @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
//        return journalCommentService.pageWithParentVoBy(journalId, PageRequest.of(page, optionService.getCommentPageSize(), sort));
//    }
//
//    @PostMapping
//    @ApiOperation("Creates a journal comment")
//    public BaseCommentDTO createCommentBy(@RequestBody JournalCommentParam journalCommentParam) {
//        JournalComment journalComment = journalCommentService.createBy(journalCommentParam);
//        return journalCommentService.convertTo(journalComment);
//    }
//
//    @PutMapping("{commentId:\\d+}/status/{status}")
//    @ApiOperation("Updates comment status")
//    public BaseCommentDTO updateStatusBy(@PathVariable("commentId") Long commentId,
//                                         @PathVariable("status") CommentStatus status) {
//        // Update comment status
//        JournalComment updatedJournalComment = journalCommentService.updateStatus(commentId, status);
//        return journalCommentService.convertTo(updatedJournalComment);
//    }
//
//    @DeleteMapping("{commentId:\\d+}")
//    @ApiOperation("Deletes comment permanently and recursively")
//    public BaseCommentDTO deleteBy(@PathVariable("commentId") Long commentId) {
//        JournalComment deletedJournalComment = journalCommentService.removeById(commentId);
//        return journalCommentService.convertTo(deletedJournalComment);
//    }
}
