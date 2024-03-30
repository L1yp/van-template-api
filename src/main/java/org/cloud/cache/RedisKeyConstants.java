package org.cloud.cache;

import java.time.Duration;
import java.util.Map;

public interface RedisKeyConstants {

    Map<String, Duration> prefixDurationMap = Map.of(
            "captcha", Duration.ofSeconds(120)
    );

}
