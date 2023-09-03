package org.cloud.web.mapper;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.cloud.model.enums.CommonStatus;
import org.cloud.web.mapper.system.RoleMapper;
import org.cloud.web.model.DO.system.RoleDO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class BasicEnumTypeHandlerTests {

    @Resource
    RoleMapper roleMapper;

    @Test
    public void contextLoad() {
        List<RoleDO> list = roleMapper.wrapper().list();
        log.info("list: {}", list);
        if (!list.isEmpty()) {
            CommonStatus status = list.get(0).getStatus();
            Assertions.assertNotNull(status);
        }
    }

}
