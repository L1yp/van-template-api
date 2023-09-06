package org.cloud.web.service.system;

import org.cloud.model.AbstractUpdateDTO;
import org.cloud.model.Converter;
import org.cloud.web.model.DO.system.DepartmentDO;
import org.cloud.web.model.DTO.out.system.DepartmentOutputDTO;

import java.util.List;

public interface IDepartmentService {

    /**
     * @return 全量部门
     */
    List<DepartmentOutputDTO> list();

    /**
     * 新增部门
     * @param param 部门信息
     */
    void insert(Converter<DepartmentDO> param);

    /**
     * 更新部门
     * @param param 部门信息
     */
    void updateByPrimaryKeySelective(AbstractUpdateDTO<DepartmentDO> param);

    /**
     * 根据ID删除部门
     * @param departmentId 部门ID
     */
    void deleteById(String departmentId);

}
