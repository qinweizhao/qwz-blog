package com.qinweizhao.blog;

import cn.hutool.crypto.digest.BCrypt;
import com.qinweizhao.blog.config.properties.MyBlogProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author qinweizhao
 * @since 2022/8/12
 */
@SpringBootTest
class ApplicationTest {


    @Autowired
    private MyBlogProperties myBlogProperties;

    @Test
    void generatorPassword() {
        String password = "admin";
        System.out.println(BCrypt.hashpw(password, BCrypt.gensalt()));
    }
    @Test
    void portalPath(){
        String frontendDirName = myBlogProperties.getFrontendDirName();
        String themeDirName = myBlogProperties.getThemeDirName();
        Path frontend = Paths.get(myBlogProperties.getWorkDir(), frontendDirName,themeDirName);
        System.out.println(frontendDirName);
        System.out.println("frontend = " + frontend);
    }

}
