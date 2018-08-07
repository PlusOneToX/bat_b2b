package com.bat.distributor.api.role;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.group.dto.data.GroupDTO;
import com.bat.distributor.api.role.dto.RoleCmd;
import com.bat.distributor.api.role.dto.RoleId;
import com.bat.distributor.api.role.dto.RoleOpenCmd;
import com.bat.distributor.api.role.dto.RoleListQry;
import com.bat.distributor.api.role.dto.data.RoleDTO;

public interface RoleServiceI {
    /**
     * 添加分销商联系人角色
     * 
     * @param cmd
     * @return
     */
    public void createRole(RoleCmd cmd);

    /**
     * 更新分销商联系人角色
     * 
     * @param cmd
     * @return
     */
    public void updateRole(RoleCmd cmd);

    /**
     * 更新分销商联系人角色状态
     * 
     * @param cmd
     * @return
     */
    public void openRole(RoleOpenCmd cmd);

    /**
     * 获取分销商联系人角色列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<GroupDTO> listRole(RoleListQry qry);

    /**
     * 根据ID删除分销商联系人角色
     * 
     * @param cmd
     * @return
     */
    public void deleteRole(RoleId cmd);

    /**
     * 根据分销商联系人角色id获取详情
     * 
     * @param qry
     * @return
     */
    public RoleDTO getRole(RoleId qry);

}
