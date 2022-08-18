package com.qinweizhao.blog.cache;

import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author qinweizhao
 * @since 2022/8/18
 */
@SpringBootTest
public class CacheStoreTest {

    @Autowired
    private AbstractStringCacheStore cacheStore;


    @Test
    void clear() {
        cacheStore.delete("options");
        System.out.println("执行结束");
    }
}
