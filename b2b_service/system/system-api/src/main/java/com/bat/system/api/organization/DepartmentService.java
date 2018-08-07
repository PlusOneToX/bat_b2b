package com.bat.system.api.organization;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.organization.dto.DepartmentCreateCmd;
import com.bat.system.api.organization.dto.DepartmentQry;
import com.bat.system.api.organization.dto.DepartmentUpdateCmd;
import com.bat.system.api.organization.dto.data.DepartmentDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:48
 */
public interface DepartmentService {
    /**
     * 添加销售部门
     *
     * @param cmd
     * @return
     */
    boolean createDepartment(DepartmentCreateCmd cmd);

    /**
     * 根据ID获取销售部门
     *
     * @param id
     * @return
     */
    DepartmentDTO getDepartmentById(Integer id);

    /**
     * 获取所有销售部门（分页）
     *
     * @param qry
     * @return
     */
    PageInfo<DepartmentDTO> listDepartment(DepartmentQry qry);

    /**
     * 根据id删除销售部门
     *
     * @param id
     * @return
     */
    boolean deleteDepartmentById(Integer id);

    /**
     * 更新销售部门
     * 
     * @param cmd
     * @return
     */
    boolean updateDepartment(DepartmentUpdateCmd cmd);

    /**
     * 组织下的部门列表(公共数据)
     * 
     * @param qry
     * @return
     */
    PageInfo<DepartmentDTO> listDepartmentByOrganization(DepartmentQry qry);

    /**
     * 同步erp部门
     */
    void syncDepartment();
}
