package org.cloud.web.service.system;

import io.mybatis.mapper.BaseMapper;
import org.cloud.model.AbstractUpdateDTO;
import org.cloud.model.Converter;
import org.cloud.model.common.PageDTO;
import org.cloud.web.model.DO.system.MenuDO;
import org.cloud.web.model.DTO.out.system.MenuOutputDTO;

import java.util.List;
import java.util.function.Consumer;

public interface IMenuService {

    BaseMapper<MenuDO, String> getBaseMapper();

    List<MenuOutputDTO> list(PageDTO param);

    MenuDO getById(String id);

    void updateByPrimaryKeySelective(AbstractUpdateDTO<MenuDO> param);

    void deleteById(String id);

    void insertSelective(Converter<MenuDO> param);

    void insert(Converter<MenuDO> param);


}
