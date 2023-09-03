package org.cloud;


import org.cloud.util.PinyinUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PinyinTests {

    @Test
    public void test() {
        Assertions.assertEquals(PinyinUtil.getPinyin("韩信"), "han xin,shen");
    }

}
