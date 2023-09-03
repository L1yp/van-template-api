package org.cloud;

import jakarta.annotation.Resource;
import org.cloud.web.mapper.system.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserPasswordTests {

    @Resource
    UserMapper userMapper;

    @Test
    public void changePwdTests() {


    }

}
