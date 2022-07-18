//package com.qinweizhao.blog.controller.admin.api;
//
//import com.qinweizhao.blog.model.dto.PhotoDTO;
//import com.qinweizhao.blog.model.entity.Photo;
//import com.qinweizhao.blog.model.params.PhotoParam;
//import com.qinweizhao.blog.model.params.PhotoQuery;
//import com.qinweizhao.blog.service.PhotoService;
//import io.swagger.annotations.ApiOperation;
//import lombok.AllArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.data.web.SortDefault;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.List;
//
//import static org.springframework.data.domain.Sort.Direction.DESC;
//
///**
// * Photo controller
// *
// * @author ryanwang
// * @author qinweizhao
// * @date 2019-03-21
// */
//@RestController
//@AllArgsConstructor
//@RequestMapping("/api/admin/photos")
//public class PhotoController {
//
//    private final PhotoService photoService;
//
//    /**
//     * 列出最新照片
//     *
//     * @param sort sort
//     * @return List
//     */
//    @GetMapping(value = "latest")
//    public List<PhotoDTO> listPhotos(@SortDefault(sort = "createTime", direction = Sort.Direction.DESC) Sort sort) {
//        return PhotoConvert.INSTANCE.convertToDTO(photoService.list());
//    }
//
//    @GetMapping
//    public Page<PhotoDTO> pageBy(@PageableDefault(sort = "createTime", direction = DESC) Pageable pageable,
//                                 PhotoQuery photoQuery) {
//        Page<Photo> page = photoService.page(null);
//        return PhotoConvert.INSTANCE.convertToDTO(page);
//    }
//
//    /**
//     * 详细信息
//     *
//     * @param photoId photoId
//     * @return PhotoDTO
//     */
//    @GetMapping("{photoId:\\d+}")
//    public PhotoDTO getBy(@PathVariable("photoId") Integer photoId) {
//        Photo photo = photoService.getById(photoId);
//        return PhotoConvert.INSTANCE.convert(photo);
//    }
//
//    @DeleteMapping("{photoId:\\d+}")
//    @ApiOperation("Deletes photo by id")
//    public void deletePermanently(@PathVariable("photoId") Integer photoId) {
//        photoService.removeById(photoId);
//    }
//
//    @PostMapping
//    @ApiOperation("Creates a photo")
//    public PhotoDTO createBy(@Valid @RequestBody PhotoParam photoParam) {
//        Photo convert = PhotoConvert.INSTANCE.convert(photoParam);
//        boolean save = photoService.save(convert);
//        return new PhotoDTO();
//    }
//
//
//    /**
//     * 更新
//     *
//     * @param photoId    photoId
//     * @param photoParam photoParam
//     * @return PhotoDTO
//     */
//    @PutMapping("{photoId:\\d+}")
//    public PhotoDTO updateBy(@PathVariable("photoId") Integer photoId,
//                             @RequestBody @Valid PhotoParam photoParam) {
//
//        Photo photo = PhotoConvert.INSTANCE.convert(photoParam);
//        photo.setId(photoId);
//        // Update menu in database
//        boolean b = photoService.updateById(photo);
//        return new PhotoDTO();
//    }
//
//    @GetMapping("teams")
//    @ApiOperation("Lists all of photo teams")
//    public List<String> listTeams() {
//        return photoService.listAllTeams();
//    }
//}
