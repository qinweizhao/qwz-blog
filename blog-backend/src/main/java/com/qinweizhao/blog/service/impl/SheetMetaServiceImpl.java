package com.qinweizhao.blog.service.impl;

import com.qinweizhao.blog.service.SheetMetaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Sheet meta service implementation class.
 *
 * @author ryanwang
 * @author ikaisec
 * @since 2019-08-04
 */
@Slf4j
@Service
public class SheetMetaServiceImpl implements SheetMetaService {
//
//    private final SheetMetaRepository sheetMetaRepository;
//
//    private final SheetRepository sheetRepository;
//
//    public SheetMetaServiceImpl(SheetMetaRepository sheetMetaRepository,
//            SheetRepository sheetRepository) {
//        super(sheetMetaRepository);
//        this.sheetMetaRepository = sheetMetaRepository;
//        this.sheetRepository = sheetRepository;
//    }

    public void validateTarget(Integer sheetId) {
//        sheetRepository.findById(sheetId)
//                .orElseThrow(() -> new NotFoundException("查询不到该页面的信息").setErrorData(sheetId));
    }
}
