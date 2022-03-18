package com.qinweizhao.blog.other;

import com.qinweizhao.blog.config.properties.HaloProperties;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.qinweizhao.blog.model.support.HaloConst.FILE_SEPARATOR;
import static com.qinweizhao.blog.model.support.HaloConst.USER_HOME;
import static com.qinweizhao.blog.utils.HaloUtils.ensureSuffix;

@SpringBootTest
public class OutputTest {

    @Test
    void test1() {
        String property = System.getProperty("user.dir");
        System.out.println(property);
    }

    @Test
    void test() {
        System.out.println("spring.config.additional-location");
        String property = System.getProperty("spring.config.additional-location");
        System.out.println(property);
    }

    @Autowired
    HaloProperties haloProperties;

    @Test
    void test3(){
        System.out.println(USER_HOME);
        System.out.println(FILE_SEPARATOR);
        System.out.println(ensureSuffix(USER_HOME, FILE_SEPARATOR));
        System.out.printf(ensureSuffix(USER_HOME, FILE_SEPARATOR) + ".halo" + FILE_SEPARATOR);


        String workDir = "file:///" + ensureSuffix(haloProperties.getWorkDir(), FILE_SEPARATOR);

        System.out.println("=========");
        String FILE_PROTOCOL = "file:///";
        System.out.println();
        String userDir = System.getProperty("user.home");
        System.out.println(userDir);
        System.out.println(
                FILE_PROTOCOL+userDir + "/Code/qwz/qwz-blog/blog-frontend/"
        );
    }



}
