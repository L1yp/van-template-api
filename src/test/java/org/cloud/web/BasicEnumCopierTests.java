package org.cloud.web;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.cloud.model.enums.CommonStatus;
import org.cloud.util.BeanCopierUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class BasicEnumCopierTests {

    @Getter
    @Setter
    public static class FromType {
        private CommonStatus status = CommonStatus.DISABLE;
    }

    @Getter
    @Setter
    public static class ToType {
        private Integer status;
    }


    @Test
    public void test() {
        var fromType = new FromType();
        var toType = new ToType();
        BeanCopierUtil.copy(fromType, toType);
        Assertions.assertEquals(toType.status, CommonStatus.DISABLE.getValue());

        toType.status = CommonStatus.ENABLE.getValue();
        BeanCopierUtil.copy(toType, fromType);
        Assertions.assertEquals(fromType.status, CommonStatus.ENABLE);




    }

}
