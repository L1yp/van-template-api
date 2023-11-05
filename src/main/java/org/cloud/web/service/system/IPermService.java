package org.cloud.web.service.system;

import org.cloud.model.AbstractUpdateDTO;
import org.cloud.model.Converter;
import org.cloud.model.common.PageDTO;
import org.cloud.mybatis.Mapper;
import org.cloud.web.model.DO.system.PermDO;
import org.cloud.web.model.DTO.out.system.PermOutputDTO;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public interface IPermService {

    Mapper<PermDO, String> getBaseMapper();

    /**
     * 通过权限标识获取权限信息
     * @param key 权限标识
     * @return 权限信息
     */
    PermDO getPermByKey(String key);

    /**
     * 通过权限标识列表查询权限详情列表
     *
     * @param permKeyList 权限标识列表
     * @return 权限详情列表
     */
    List<PermOutputDTO> listByPermKeyList(Collection<String> permKeyList);


    /**
     * 通过权限标识删除权限信息
     * @param permKey 权限标识
     */
    void deleteByPermKey(String permKey);

    /**
     * 获取所有权限信息
     * @param param ignored
     * @return 所有权限列表
     */
    List<PermOutputDTO> list(PageDTO param);

    /**
     * 通过权限ID获取权限信息
     * @param id 权限ID
     * @return 权限信息
     */
    PermDO getById(String id);

    /**
     * 更新权限信息
     * @param param {@link org.cloud.web.model.DTO.in.system.PermUpdateDTO}
     */
    void updateByPrimaryKeySelective(AbstractUpdateDTO<PermDO> param);

    /**
     * 通过权限ID删除权限
     * @param id 权限ID
     */
    void deleteById(String id);

    /**
     * 新增权限
     * @param modelDO 权限信息
     */
    void insert(PermDO modelDO);

    void insertSelective(PermDO modelDO);

    /**
     * 新增权限
     * @param param {@link org.cloud.web.model.DTO.in.system.PermAddDTO}
     */
    void insertSelective(Converter<PermDO> param);

    /**
     * 新增权限
     * @param param {@link org.cloud.web.model.DTO.in.system.PermAddDTO}
     * @param consumer DO预处理
     */
    void insertSelective(Converter<PermDO> param, Consumer<PermDO> consumer);


}
