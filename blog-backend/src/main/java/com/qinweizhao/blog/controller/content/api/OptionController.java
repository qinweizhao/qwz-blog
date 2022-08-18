package com.qinweizhao.blog.controller.content.api;

import com.qinweizhao.blog.model.dto.ConfigDTO;
import com.qinweizhao.blog.model.support.BaseResponse;
import com.qinweizhao.blog.service.ConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Content option controller.
 *
 * @author johnniang
 * @since 2019-04-03
 */
@RestController("ApiContentOptionController")
@RequestMapping("/api/content/options")
public class OptionController {

    private final ConfigService configService;

    public OptionController(ConfigService configService) {
        this.configService = configService;
    }

//    @GetMapping("list_view")
//    public List<ConfigDTO> listAll() {
//        return configService.listDtos();
//    }
//
//    @GetMapping("map_view")
//    public Map<String, Object> listAllWithMapView(@RequestParam(value = "key", required = false) List<String> keys) {
//        if (CollectionUtils.isEmpty(keys)) {
//            return configService.listOptions();
//        }
//
//        return configService.listOptions(keys);
//    }
//
//    @GetMapping("keys/{key}")
//    public BaseResponse<Object> getBy(@PathVariable("key") String key) {
//        return BaseResponse.ok(HttpStatus.OK.getReasonPhrase(), configService.getByKey(key).orElse(null));
//    }
//
//
//    @GetMapping("comment")
//    public Map<String, Object> comment() {
//        List<String> keys = new ArrayList<>();
//        keys.add("comment_gravatar_default");
//        keys.add("comment_content_placeholder");
//        return configService.listOptions(keys);
//    }
}
