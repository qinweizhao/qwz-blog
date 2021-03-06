//package com.qinweizhao.blog.service.impl;
//
//import lombok.extern.slf4j.Slf4j;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.stereotype.Service;
//import com.qinweizhao.blog.exception.NotFoundException;
//import com.qinweizhao.blog.repository.PostRepository;
//import com.qinweizhao.blog.repository.base.BaseMetaRepository;
//import com.qinweizhao.blog.service.PostMetaService;
//
///**
// * Post meta service implementation class.
// *
// * @author ryanwang
// * @author ikaisec
// * @author guqing
// * @date 2019-08-04
// */
//@Slf4j
//@Service
//public class PostMetaServiceImpl extends BaseMetaServiceImpl<PostMeta> implements PostMetaService {
//
//    private final PostRepository postRepository;
//
//    public PostMetaServiceImpl(BaseMetaRepository<PostMeta> baseMetaRepository, PostRepository postRepository) {
//        super(baseMetaRepository);
//        this.postRepository = postRepository;
//    }
//
//    @Override
//    public void validateTarget(@NotNull Integer postId) {
//        postRepository.findById(postId)
//                .orElseThrow(() -> new NotFoundException("查询不到该文章的信息").setErrorData(postId));
//    }
//}
