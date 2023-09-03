package org.cloud.util;

import java.util.UUID;

public class TokenUtil {

    public static String genToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
