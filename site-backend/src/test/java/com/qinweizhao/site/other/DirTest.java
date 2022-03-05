package com.qinweizhao.site.other;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DirTest {

    @Test
    void test1(){
        String property = System.getProperty("user.dir");
        System.out.println(property);
    }
}
