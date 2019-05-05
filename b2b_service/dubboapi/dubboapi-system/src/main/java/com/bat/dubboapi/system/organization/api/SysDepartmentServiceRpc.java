package com.bat.dubboapi.system.organization.api;

import com.bat.dubboapi.system.organization.dto.data.DepartmentRpcDTO;
import com.bat.dubboapi.system.common.Response;

import java.util.List;

public interface SysDepartmentServiceRpc {

    /**
     * 根据状态查询部门列表
     * 
     * @param status
     * @return
     */
    Response<List<DepartmentRpcDTO>> listByCondition(Short status);

    /**
     * 根据部门id 获取部门信息
     * 
     * @param id
     * @return
     */
    Response<DepartmentRpcDTO> getDepartmentById(Integer id);

    /**
     * 根据部门id 获取部门信息
     *
     * @param ids
     * @return
     */
    Response<List<DepartmentRpcDTO>> getDepartmentByIds(List<Integer> ids);
}
