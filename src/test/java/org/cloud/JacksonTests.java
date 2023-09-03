package org.cloud;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.cloud.util.JSON;
import org.cloud.web.mapper.system.UserMapper;
import org.cloud.web.model.DO.system.UserDO;
import org.cloud.web.model.DTO.out.system.UserLoginResultDTO;
import org.cloud.web.model.DTO.out.system.UserOutputDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@Slf4j
@SpringBootTest
public class JacksonTests {


    @Resource
    UserMapper userMapper;

    @Test
    public void testUserInfo() {
        var result = new UserLoginResultDTO();

        UserDO userDO = userMapper.selectByPrimaryKey("1").orElse(null);
        UserOutputDTO outputDTO = userDO.convert();
        result.setUserInfo(outputDTO);
        result.setRoleIdList(Arrays.asList("1", "2"));

        String jsonString = JSON.toJSONString(result);

        log.info(jsonString);
    }

}
