package com.qinweizhao.blog.service.impl;

import org.springframework.stereotype.Service;

/**
 * Sheet comment service implementation.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-04-24
 */
@Service
public class SheetCommentServiceImpl {

//
//    @Override
//    public void validateTarget(Integer sheetId) {
//        Sheet sheet = sheetRepository.findById(sheetId)
//                .orElseThrow(() -> new NotFoundException("查询不到该页面的信息").setErrorData(sheetId));
//
//        if (sheet.getDisallowComment()) {
//            throw new BadRequestException("该页面已被禁止评论").setErrorData(sheetId);
//        }
//    }
//
//    @Override
//    public SheetCommentWithSheetVO convertToWithSheetVo(SheetComment comment) {
//        Assert.notNull(comment, "SheetComment must not be null");
//        SheetCommentWithSheetVO sheetCommentWithSheetVO = new SheetCommentWithSheetVO().convertFrom(comment);
//
//        BasePostMinimalDTO basePostMinimalDTO = new BasePostMinimalDTO().convertFrom(sheetRepository.getOne(comment.getPostId()));
//
//        sheetCommentWithSheetVO.setSheet(buildSheetFullPath(basePostMinimalDTO));
//        return sheetCommentWithSheetVO;
//    }
//
//    @Override
//    public List<SheetCommentWithSheetVO> convertToWithSheetVo(List<SheetComment> sheetComments) {
//        if (CollectionUtils.isEmpty(sheetComments)) {
//            return Collections.emptyList();
//        }
//
//        Set<Integer> sheetIds = ServiceUtils.fetchProperty(sheetComments, SheetComment::getPostId);
//
//        Map<Integer, Sheet> sheetMap = ServiceUtils.convertToMap(sheetRepository.findAllById(sheetIds), Sheet::getId);
//
//        return sheetComments.stream()
//                .filter(comment -> sheetMap.containsKey(comment.getPostId()))
//                .map(comment -> {
//                    SheetCommentWithSheetVO sheetCmtWithPostVO = new SheetCommentWithSheetVO().convertFrom(comment);
//
//                    BasePostMinimalDTO postMinimalDTO = new BasePostMinimalDTO().convertFrom(sheetMap.get(comment.getPostId()));
//
//                    sheetCmtWithPostVO.setSheet(buildSheetFullPath(postMinimalDTO));
//                    return sheetCmtWithPostVO;
//                })
//                .collect(Collectors.toList());
//    }
//
//    private BasePostMinimalDTO buildSheetFullPath(BasePostMinimalDTO basePostMinimalDTO) {
//        StringBuilder fullPath = new StringBuilder();
//
//        if (optionService.isEnabledAbsolutePath()) {
//            fullPath.append(optionService.getBlogBaseUrl());
//        }
//
//        fullPath.append(URL_SEPARATOR)
//                .append(optionService.getSheetPrefix())
//                .append(URL_SEPARATOR)
//                .append(basePostMinimalDTO.getSlug())
//                .append(optionService.getPathSuffix());
//
//        basePostMinimalDTO.setFullPath(fullPath.toString());
//        return basePostMinimalDTO;
//    }
//
//    @Override
//    public Page<SheetCommentWithSheetVO> convertToWithSheetVo(Page<SheetComment> sheetCommentPage) {
//        Assert.notNull(sheetCommentPage, "Sheet comment page must not be null");
//
//        return new PageImpl<>(convertToWithSheetVo(sheetCommentPage.getContent()), sheetCommentPage.getPageable(), sheetCommentPage.getTotalElements());
//
//    }
}
