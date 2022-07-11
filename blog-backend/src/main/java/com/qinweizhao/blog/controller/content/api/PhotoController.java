//package com.qinweizhao.blog.controller.content.api;
//
//import com.qinweizhao.blog.convert.PhotoConvert;
//import com.qinweizhao.blog.model.dto.PhotoDTO;
//import com.qinweizhao.blog.model.params.PhotoQuery;
//import com.qinweizhao.blog.service.PhotoService;
//import lombok.AllArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.data.web.SortDefault;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//import static org.springframework.data.domain.Sort.Direction.DESC;
//
///**
// * Content photo controller.
// *
// * @author ryanwang
// * @date 2019-09-21
// */
//@RestController("ApiContentPhotoController")
//@AllArgsConstructor
//@RequestMapping("/api/content/photos")
//public class PhotoController {
//
//    private final PhotoService photoService;
//
//    /**
//     * List all photos
//     *
//     * @param sort sort
//     * @return all of photos
//     */
//    @GetMapping(value = "latest")
//    public List<PhotoDTO> listPhotos(@SortDefault(sort = "updateTime", direction = Sort.Direction.DESC) Sort sort) {
//        return PhotoConvert.INSTANCE.convertToDTO(photoService.list());
//    }
//
//    @GetMapping
//    public Page<PhotoDTO> pageBy(@PageableDefault(sort = "updateTime", direction = DESC) Pageable pageable,
//                                 PhotoQuery photoQuery) {
////        pageable, photoQuery
//        return photoService.page(null);
//    }
//}
