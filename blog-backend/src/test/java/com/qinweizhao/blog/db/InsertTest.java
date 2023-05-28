package com.qinweizhao.blog.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author qinweizhao
 * @since 2023-05-22
 */
@SpringBootTest
class InsertTest {
//
//    @Autowired
//    CommentMapper commentMapper;
//
//
//    @Test
//    @Transactional
//    void test() {
//        Comment comment = new Comment();
//        comment.setId(0L);
//        comment.setType(0);
//        comment.setAllowNotification(false);
//        comment.setAuthor("");
//        comment.setAuthorUrl("");
//        comment.setContent("");
//        comment.setEmail("");
//        comment.setGravatarMd5("");
//        comment.setIpAddress("");
//        comment.setIsAdmin(false);
//        comment.setParentId(0L);
//        comment.setTargetId(0);
//        comment.setStatus(0);
//        comment.setTopPriority(0);
//        comment.setUserAgent("");
//        comment.setCreateTime(LocalDateTime.now());
//        comment.setUpdateTime(LocalDateTime.now());
//
//        int insert = commentMapper.insert(comment);
//
//        if (insert == 1) {
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        // 模拟异常
//        int i = 10 / 0;
//
//        System.out.println("程序结束");
//
//    }

}
