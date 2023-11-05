package org.cloud;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.cloud.util.JSON;
import org.cloud.web.model.DO.system.UserDO;
import org.cloud.web.service.system.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class CachingTests {

    @Resource
    UserServiceImpl service;


    @Test
    public void contextLoad() {
        UserDO userDO = service.getById("1");
        log.info("data: {}", JSON.toJSONString(userDO));
        UserDO user2 = service.getByUserName("admin");
        log.info("data: {}", JSON.toJSONString(user2));
    }

}
