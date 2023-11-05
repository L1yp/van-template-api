package org.cloud;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.cloud.web.mapper.system.PermMapper;
import org.cloud.web.model.DO.system.PermDO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class ListMapperTests {

    @Resource
    PermMapper permMapper;

    @Test
    @Transactional
    public void contextLoad() {

        PermDO permDO = new PermDO();
        permDO.setName("1");
        permDO.setPermKey("permKey1");
        permDO.setCreateBy("1");
        permDO.setOrderNo(10);
        permDO.setCreateTime(LocalDateTime.now());
        List<PermDO> list = new ArrayList<>();
        list.add(permDO);
        permDO = new PermDO();
        permDO.setName("2");
        permDO.setPermKey("permKey2");
        permDO.setCreateBy("1");
        permDO.setOrderNo(11);
        permDO.setCreateTime(LocalDateTime.now());
        list.add(permDO);
        permMapper.insertList(list);

        Assertions.assertNotNull(list.get(0).getId());
        Assertions.assertNotNull(list.get(1).getId());
        log.info("id1: {}", list.get(0).getId());
        log.info("id2: {}", list.get(1).getId());

        permMapper.wrapper().eq(PermDO::getPermKey, "permKey1").delete();
        permMapper.wrapper().eq(PermDO::getPermKey, "permKey2").delete();

    }


}
