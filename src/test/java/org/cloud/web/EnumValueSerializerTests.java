package org.cloud.web;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.cloud.model.enums.CommonStatus;
import org.cloud.util.JSON;
import org.junit.jupiter.api.Test;

@Slf4j
public class EnumValueSerializerTests {

    public static class POJOTest {
        public CommonStatus status = CommonStatus.ENABLE;
    }

    @Test
    public void test() throws JsonProcessingException {
        String str = JSON.mapper.writeValueAsString(new POJOTest());
        log.info("result: {}", str);
    }


}
