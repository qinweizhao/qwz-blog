package com.qinweizhao.blog.other;

import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.framework.listener.StartedListener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static com.qinweizhao.blog.model.support.BlogConst.FILE_SEPARATOR;
import static com.qinweizhao.blog.model.support.BlogConst.USER_HOME;
import static com.qinweizhao.blog.util.HaloUtils.ensureSuffix;

@SpringBootTest
public class OutputTest {

    @Autowired
    MyBlogProperties myBlogProperties;
    @Autowired
    StartedListener startedListener;

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

    @Test
    void test3() {
        System.out.println(USER_HOME);
        System.out.println(FILE_SEPARATOR);
        System.out.println(ensureSuffix(USER_HOME, FILE_SEPARATOR));
        System.out.printf(ensureSuffix(USER_HOME, FILE_SEPARATOR) + ".halo" + FILE_SEPARATOR);


        String workDir = "file:///" + ensureSuffix(myBlogProperties.getWorkDir(), FILE_SEPARATOR);

        System.out.println("=========");
        String FILE_PROTOCOL = "file:///";
        System.out.println();
        String userDir = System.getProperty("user.home");
        System.out.println(userDir);
        System.out.println(
                FILE_PROTOCOL + userDir + "/Code/qwz/qwz-blog/blog-frontend/"
        );
    }

    @Test
    void testPaths() throws Exception {
        String path = ResourceUtils.CLASSPATH_URL_PREFIX + "templates/common";
        URI uri = ResourceUtils.getURL(path).toURI();
        System.out.println("Paths = " + uri);

    }


    @Test
    void print() {
        List<Object> objects = Collections.emptyList();
        System.out.println(objects);
    }


}
