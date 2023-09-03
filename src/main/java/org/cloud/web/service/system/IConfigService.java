package org.cloud.web.service.system;

import org.cloud.web.model.DTO.out.system.ConfigEnumOutputDTO;

import java.util.List;
import java.util.Map;

public interface IConfigService {

    /**
     * 获取所有枚举信息
     * @return 枚举信息: [枚举标识 => [枚举值 => 枚举信息] ]
     */
    Map<String, List<ConfigEnumOutputDTO>> getBasicEnumList();


    /**
     * 获取BasicEnum枚举列表
     * @return 枚举class列表
     */
    List<Class<?>> getBasicEnumClassList();

}
