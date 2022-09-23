package com.qinweizhao.blog;

import cn.hutool.crypto.digest.BCrypt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author qinweizhao
 * @since 2022/8/12
 */
@SpringBootTest
class ApplicationTest {


    @Test
    void generatorPassword(){
        String password= "admin";
        System.out.println(BCrypt.hashpw(password, BCrypt.gensalt()));
    }

}
